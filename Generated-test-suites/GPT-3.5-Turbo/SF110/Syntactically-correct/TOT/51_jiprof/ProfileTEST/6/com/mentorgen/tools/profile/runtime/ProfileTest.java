
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import com.mentorgen.tools.profile.output.*;

public class ProfileTest {

    @Test
    public void testInitProfiler() {
        // Typical use case
        // Verify that the initProfiler method initializes the necessary objects and starts the controller thread
        Profile.initProfiler();
        assertNotNull(Profile._controller);
        assertTrue(Profile._controllerThread.isAlive());
    }

    @Test
    public void testInit() {
        // Typical use case
        // Verify that the init method creates new instances of the necessary objects
        Profile.init();
        assertNotNull(Profile._threadActiveFrame);
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._lock);
        assertNotNull(Profile._allocList);
    }

    @Test
    public void testClear() {
        // Typical use case
        // Verify that the clear method resets the necessary objects by calling init method
        // and clears the frameList and allocList
        Profile.clear();
        verifyInitMethodCalled();
        assertEquals(0, Profile._frameList.size());
        assertEquals(0, Profile._allocList.size());
    }

    @Test
    public void testStart() {
        // Typical use case
        // Verify that the start method starts the controller
        Profile.start();
        assertTrue(Profile._controller.isRunning());
    }

    @Test
    public void testStop() {
        // Typical use case
        // Verify that the stop method stops the controller
        Profile.stop();
        assertFalse(Profile._controller.isRunning());
    }

    @Test
    public void testSetFileName() {
        // Typical use case
        // Verify that the setFileName method sets the fileName in the controller
        final String fileName = "test_profile";
        Profile.setFileName(fileName);
        assertEquals(fileName, Profile._controller.getFileName());
    }

    @Test
    public void testShutdown() {
        // Typical use case
        // Verify that the shutdown method sets _profile to false, closes all frames, and computes net time
        Profile.shutdown();
        assertFalse(Profile._controller.isProfiling());
        // Verify all frames are closed
        for (Frame frame : Profile.frameList()) {
            assertTrue(frame.isClosed());
        }
        // Verify net time is computed for all frames
        for (Frame frame : Profile.frameList()) {
            assertTrue(frame.getNetTime() >= 0);
        }
    }

    @Test
    public void testThreads() {
        // Typical use case
        // Verify that the threads method returns the thread dictionary threads
        Iterable<Long> threads = Profile.threads();
        assertNotNull(threads);
        // Verify threadId is not null
        for (Long threadId : threads) {
            assertNotNull(threadId);
        }
    }

    @Test
    public void testInteractions() {
        // Typical use case
        // Verify that the interactions method returns the interactions for the given threadId
        long threadId = 1L; // Assuming threadId 1 exists in the thread dictionary
        Iterable<Frame> interactions = Profile.interactions(threadId);
        assertNotNull(interactions);
        // Verify frame is not null
        for (Frame frame : interactions) {
            assertNotNull(frame);
        }
    }

    @Test
    public void testFrameList() {
        // Typical use case
        // Verify that the frameList method returns the frame list
        Iterable<Frame> frameList = Profile.frameList();
        assertNotNull(frameList);
        // Verify frame is not null
        for (Frame frame : frameList) {
            assertNotNull(frame);
        }
    }

    @Test
    public void testAllocations() {
        // Typical use case
        // Verify that the allocations method returns the allocations list
        Iterable<ClassAllocation> allocations = Profile.allocations();
        assertNotNull(allocations);
        // Verify allocation is not null
        for (ClassAllocation allocation : allocations) {
            assertNotNull(allocation);
        }
    }

    @Test
    public void testGetThreadTotalTime() {
        // Typical use case
        // Verify that the getThreadTotalTime method returns the total time for the given threadId
        long threadId = 1L; // Assuming threadId 1 exists in the thread dictionary
        long totalTime = Profile.getThreadTotalTime(threadId);
        assertTrue(totalTime >= 0);
    }

    @Test
    public void testSortFrameList() {
        // Typical use case
        // Verify that the sortFrameList method sorts the frame list using the given Comparator
        Comparator<Frame> comp = new Comparator<Frame>() {
            public int compare(Frame f1, Frame f2) {
                return Long.compare(f1.getEndTime(), f2.getEndTime());
            }
        };
        Profile.sortFrameList(comp);
        List<Frame> sortedFrameList = new ArrayList<>(Profile.frameList());
        for (int i = 1; i < sortedFrameList.size(); i++) {
            assertTrue(sortedFrameList.get(i-1).getEndTime() <= sortedFrameList.get(i).getEndTime());
        }
    }

    @Test
    public void testStartStringString() {
        // Typical use case
        // Verify that the start method starts a new frame with the given className and methodName
        final String className = "TestClass";
        final String methodName = "testMethod";
        Profile.start(className, methodName);
        Frame target = findFrameForCurrentThread(className, methodName);
        assertNotNull(target);
        assertEquals(className, target.getClassName());
        assertEquals(methodName, target.getMethodName());
        assertNull(target.getParent());
        assertEquals(Thread.currentThread().getId(), target.getThreadId());
        assertTrue(target.getBeginTime() >= 0);
    }

    @Test
    public void testEndStringString() {
        // Typical use case
        // Verify that the end method ends the current frame with the given className and method
        final String className = "TestClass";
        final String methodName = "testMethod";
        Profile.end(className, methodName);
        Frame target = findFrameForCurrentThread(className, methodName);
        assertNull(target);
    }

    @Test
    public void testBeginWaitStringString() {
        // Typical use case
        // Verify that the beginWait method starts waiting in the current frame with the given className and method
        final String className = "TestClass";
        final String methodName = "testMethod";
        Profile.beginWait(className, methodName);
        Frame target = findFrameForCurrentThread(className, methodName);
        assertNotNull(target);
        assertTrue(target.getWaitStartTime() >= 0);
    }

    @Test
    public void testEndWaitStringString() {
        // Typical use case
        // Verify that the endWait method ends the waiting in the current frame with the given className and method
        final String className = "TestClass";
        final String methodName = "testMethod";
        Profile.endWait(className, methodName);
        Frame target = findFrameForCurrentThread(className, methodName);
        assertNotNull(target);
        assertNotNull(target.getWaitStartTime());
    }

    @Test
    public void testUnwind() {
        // Typical use case
        // Verify that the unwind method sets the thread active frame to target frame with
        // the given className, methodName, and exception
        final String className = "TestClass";
        final String methodName = "testMethod";
        final String exception = "TestException";
        Profile.unwind(className, methodName, exception);
        Frame target = findFrameForCurrentThread(className, methodName);
        assertNotNull(target);
        assertEquals(target, Profile._threadActiveFrame.get(Thread.currentThread().getId()));
    }

    @Test
    public void testAlloc() {
        // Typical use case
        // Verify that the alloc method increments the allocation count of the class with the given className
        final String className = "TestClass";
        Profile.alloc(className);
        ClassAllocation ca = Profile._allocList.get(className);
        assertNotNull(ca);
        assertEquals(1, ca.getAllocationCount());
    }

    // Helper methods

    private void verifyInitMethodCalled() {
        assertNull(Profile._threadActiveFrame);
        assertNull(Profile._threadDictionary);
        assertNull(Profile._methodDictionary);
        assertNull(Profile._frameList);
        assertNull(Profile._lock);
        assertNull(Profile._allocList);
        Profile.init();
    }
    
    private Frame findFrameForCurrentThread(final String className, final String methodName) {
        long threadId = Thread.currentThread().getId();
        Frame targetFrame = null;
        for (Frame frame : Profile.frameList()) {
            if (frame.getThreadId() == threadId &&
                frame.getClassName().equals(className) &&
                frame.getMethodName().equals(methodName)) {
                targetFrame = frame;
                break;
            }
        }
        return targetFrame;
    }
}

