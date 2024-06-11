
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileTest {
    @Before
    public void init() {
        // Initialize Profile class before each test
        Profile.init();
    }

    @Test
    public void testInit() {
        // Test if Profile has been properly initialized
        assertNotNull(Profile._threadActiveFrame);
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._lock);
        assertNotNull(Profile._allocList);
    }

    @Test
    public void testClear() {
        // Test if clear() method re-initializes Profile
        Profile.clear();
        testInit();
    }
    
    @Test
    public void testStartAndStop() {
        // Test if start() and stop() methods work properly
        Profile.start();
        assertTrue(Profile._controller.isRunning());
        Profile.stop();
        assertFalse(Profile._controller.isRunning());
    }
    
    @Test
    public void testSetFileName() {
        // Test if setFileName() method properly changes file name
        String fileName = "testFileName";
        Profile.setFileName(fileName);
        assertEquals(fileName, Profile._controller.getFileName());
    }
    
    @Test
    public void testShutdown() {
        // Test if shutdown() method sets profile to false and closes frames
        Profile._profile = true; // Set profile to true for testing
        Profile.shutdown();
        assertFalse(Profile._profile); // profile should be set to false
        for (Frame frame : Profile.frameList()) {
            assertTrue(frame.isClosed()); // all frames should be closed
            frame.computeNetTime(); // frame's net time should be computed
        }
    }

    @Test
    public void testThreads() {
        // Test if threads() method returns all threads in threadDictionary
        Iterable<Long> threads = Profile.threads();
        for (Long thread : threads) {
            assertTrue(Profile._threadDictionary.contains(thread));
        }
    }
    
    @Test
    public void testInteractions() {
        // Test if interactions() method returns all frames for a thread
        String className = "TestClass";
        String methodName = "testMethod";
        long threadId = 12345L;
        
        // Create a new frame for the thread
        Frame frame = new Frame(null, new Method(className, methodName), threadId);
        Profile._threadDictionary.add(threadId, frame);
        
        Iterable<Frame> interactions = Profile.interactions(threadId);
        boolean containsFrame = false;
        for (Frame interaction : interactions) {
            if (interaction == frame) {
                containsFrame = true;
                break;
            }
        }
        assertTrue(containsFrame);
    }
    
    @Test
    public void testFrameList() {
        // Test if frameList() method returns all frames in frameList
        Iterable<Frame> frameList = Profile.frameList();
        for (Frame frame : frameList) {
            assertTrue(Profile._frameList.contains(frame));
        }
    }

    @Test
    public void testAllocations() {
        // Test if allocations() method returns all ClassAllocations in allocList
        Iterable<ClassAllocation> allocations = Profile.allocations();
        for (ClassAllocation allocation : allocations) {
            assertTrue(Profile._allocList.containsValue(allocation));
        }
    }
    
    @Test
    public void testGetThreadTotalTime() {
        // Test if getThreadTotalTime() returns the total time for a given thread
        long threadId = 12345L;
        long totalTime = 1000L;
        
        // Set threadTotalTime for the specified thread
        Profile._threadDictionary.setThreadTotalTime(threadId, totalTime);
        
        assertEquals(totalTime, Profile.getThreadTotalTime(threadId));
    }
    
    @Test
    public void testSortFrameList() {
        // Test if sortFrameList() sorts frameList using the provided comparator
        List<Frame> sortedFrameList = new ArrayList<>(Profile._frameList);
        Comparator<Frame> comparator = new Comparator<Frame>() {
            public int compare(Frame f1, Frame f2) {
                return f1.getId() - f2.getId();
            }
        };
        Collections.sort(sortedFrameList, comparator);
        
        Profile.sortFrameList(comparator);
        assertEquals(sortedFrameList, Profile._frameList);
    }
    
    @Test
    public void testStart() {
        // Test if start() method creates a new frame and adds it to frameList, threadActiveFrame and threadDictionary
        String className = "TestClass";
        String methodName = "testMethod";
        
        // Start a method
        Profile.start(className, methodName);
        
        // Get the latest frame for the current thread
        Frame frame = Profile._threadDictionary.getMostRecentFrame(Thread.currentThread().getId());
        
        assertEquals(className, frame.getClassName());
        assertEquals(methodName, frame.getMethodName());
    }
    
    @Test
    public void testEnd() {
        // Test if end() method sets the end time of the frame with specified className and method
        String className = "TestClass";
        String methodName = "testMethod";
        
        Profile.start(className, methodName); // Start a method
        Frame frame = Profile._threadDictionary.getMostRecentFrame(Thread.currentThread().getId()); // Get the latest frame for the current thread
        Profile.end(className, methodName);
        
        assertTrue(frame.getEndTime() > frame.getBeginTime());
    }
    
    @Test
    public void testBeginWait() {
        // Test if beginWait() method sets begin wait time of the frame with specified className and method
        String className = "TestClass";
        String methodName = "testMethod";
        
        Profile.start(className, methodName); // Start a method
        Frame frame = Profile._threadDictionary.getMostRecentFrame(Thread.currentThread().getId()); // Get the latest frame for the current thread
        
        long beginWaitTime = System.nanoTime();
        Profile.beginWait(className, methodName);
        
        assertTrue(frame.getBeginWaitTime() >= beginWaitTime);
    }
    
    @Test
    public void testEndWait() {
        // Test if endWait() method sets end wait time of the frame with specified className and method
        String className = "TestClass";
        String methodName = "testMethod";
        
        Profile.start(className, methodName); // Start a method
        Frame frame = Profile._threadDictionary.getMostRecentFrame(Thread.currentThread().getId()); // Get the latest frame for the current thread
        
        long endWaitTime = System.nanoTime();
        Profile.endWait(className, methodName);
        
        assertTrue(frame.getEndWaitTime() >= endWaitTime);
    }
    
    @Test
    public void testUnwind() {
        // Test if unwind() method sets threadActiveFrame to the target frame
        String className = "TestClass";
        String methodName = "testMethod";
        
        Profile.start(className, methodName); // Start a method
        Frame frame = Profile._threadDictionary.getMostRecentFrame(Thread.currentThread().getId()); // Get the latest frame for the current thread
        
        Profile.unwind(className, methodName, "Exception");
        
        assertEquals(frame, Profile._threadActiveFrame.get(Thread.currentThread().getId()));
    }
    
    @Test
    public void testAlloc() {
        // Test if alloc() method increments allocCount of ClassAllocation with specified className
        String className = "TestClass";
        
        Profile.alloc(className);
        ClassAllocation allocation = Profile._allocList.get(className);
        
        assertNotNull(allocation);
        assertEquals(1, allocation.getAllocCount());
    }
    
    @Test
    public void testRun() {
        // Test if run() method shuts down profile, closes frames, and closes controller
        Profile._threadDictionary.add(Thread.currentThread().getId(), new Frame(null, new Method("TestClass", "testMethod"), Thread.currentThread().getId())); // Add a sample frame
        
        Profile profile = new Profile();
        profile.run();
        
        assertFalse(Profile._profile);
        for (Frame frame : Profile.frameList()) {
            assertTrue(frame.isClosed());
            frame.computeNetTime();
        }
        
        assertFalse(Profile._controller.isRunning());
    }
}

