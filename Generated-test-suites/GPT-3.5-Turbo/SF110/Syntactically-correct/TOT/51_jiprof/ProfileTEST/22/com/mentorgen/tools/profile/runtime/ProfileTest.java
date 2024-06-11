import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class ProfileTest {
    
    @Before
    public void setUp() {
        Profile.clear();
    }
    
    @Test
    public void testInitProfiler() {
        // Typical use case
        Profile.initProfiler();
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._controller);
        
        // Edge case - already initialized
        Profile.initProfiler();
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._controller);
    }
    
    @Test
    public void testInit() {
        Profile.init();
        
        // Typical use case
        assertNotNull(Profile._threadActiveFrame);
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._lock);
        assertNotNull(Profile._allocList);
        
        // Edge case - already initialized
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
        Profile.clear();
        
        // Typical use case
        assertNotNull(Profile._threadActiveFrame);
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._lock);
        assertNotNull(Profile._allocList);
        
        // Edge case - already cleared
        Profile.clear();
        assertNotNull(Profile._threadActiveFrame);
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._lock);
        assertNotNull(Profile._allocList);
    }
    
    @Test
    public void testStart() {
        Profile.start();
        
        // Typical use case
        assertTrue(Profile._controller.isRunning());
        
        // Edge case - already started
        Profile.start();
        assertTrue(Profile._controller.isRunning());
    }
    
    @Test
    public void testStop() {
        Profile.stop();
        
        // Typical use case
        assertFalse(Profile._controller.isRunning());
        
        // Edge case - already stopped
        Profile.stop();
        assertFalse(Profile._controller.isRunning());
    }
    
    @Test
    public void testSetFileName() {
        Profile.setFileName("test.txt");
        
        // Typical use case
        assertEquals("test.txt", Profile._controller.getFileName());
    }
    
    @Test
    public void testShutdown() {
        // Typical use case
        Profile.shutdown();
        assertFalse(Controller._profile);
        
        // Edge case - no frames
        Profile.shutdown();
        assertFalse(Controller._profile);
    }
    
    @Test
    public void testThreads() {
        Iterable<Long> threads = Profile.threads();
        
        // Typical use case
        assertNotNull(threads);
        
        // Edge case - no threads
        assertEquals(0, ((Collection<?>)threads).size());
    }
    
    @Test
    public void testInteractions() {
        // Prepare data
        Profile.start("ClassA", "methodA");
        Profile.end("ClassA", "methodA");
        
        long threadId = Thread.currentThread().getId();
        Iterable<Frame> frameList = Profile.interactions(threadId);
        
        // Typical use case
        assertNotNull(frameList);
        
        // Edge case - no interactions
        assertEquals(0, ((Collection<?>)frameList).size());
    }
    
    @Test
    public void testFrameList() {
        Iterable<Frame> frameList = Profile.frameList();
        
        // Typical use case
        assertNotNull(frameList);
        
        // Edge case - no frames
        assertEquals(0, ((Collection<?>)frameList).size());
    }
    
    @Test
    public void testAllocations() {
        Iterable<ClassAllocation> allocations = Profile.allocations();
        
        // Typical use case
        assertNotNull(allocations);
        
        // Edge case - no allocations
        assertEquals(0, ((Collection<?>)allocations).size());
    }
    
    @Test
    public void testGetThreadTotalTime() {
        // Prepare data
        Profile.start("ClassA", "methodA");
        Profile.end("ClassA", "methodA");
        
        long threadId = Thread.currentThread().getId();
        long totalTime = Profile.getThreadTotalTime(threadId);
        
        // Typical use case
        assertEquals(0, totalTime);
        
        // Edge case - no frames
        threadId = 1234L;
        totalTime = Profile.getThreadTotalTime(threadId);
        assertEquals(0, totalTime);
    }
    
    @Test
    public void testSortFrameList() {
        // Prepare data
        Profile.start("ClassA", "methodA");
        Profile.end("ClassA", "methodA");
        Profile.start("ClassB", "methodB");
        Profile.end("ClassB", "methodB");
        
        // Typical use case
        Profile.sortFrameList((f1, f2) -> f1.getMethodName().compareTo(f2.getMethodName()));
        assertNotNull(Profile._frameList);
        
        // Edge case - empty frame list
        Profile.clear();
        Profile.sortFrameList((f1, f2) -> f1.getMethodName().compareTo(f2.getMethodName()));
        assertNotNull(Profile._frameList);
    }
    
    @Test
    public void testStartMethod() {
        // Prepare data
        Profile.start("ClassA", "methodA");
        
        // Typical use case
        assertNotNull(Profile._threadActiveFrame.get(Thread.currentThread().getId()));
        
        // Edge case - already started
        Profile.start("ClassA", "methodA");
        assertNotNull(Profile._threadActiveFrame.get(Thread.currentThread().getId()));
    }
    
    @Test
    public void testEndMethod() {
        // Prepare data
        Profile.start("ClassA", "methodA");
        Profile.end("ClassA", "methodA");
        
        // Typical use case
        assertNull(Profile._threadActiveFrame.get(Thread.currentThread().getId()));
        
        // Edge case - no active frame
        Profile.end("ClassA", "methodA");
        assertNull(Profile._threadActiveFrame.get(Thread.currentThread().getId()));
    }
    
    @Test
    public void testBeginWait() {
        // Prepare data
        Profile.start("ClassA", "methodA");
        
        // Typical use case
        Profile.beginWait("ClassA", "methodA");
        assertNotNull(Profile._threadActiveFrame.get(Thread.currentThread().getId()));
        
        // Edge case - non-existing frame
        Profile.beginWait("ClassB", "methodB");
        assertNull(Profile._threadActiveFrame.get(Thread.currentThread().getId()));
    }
    
    @Test
    public void testEndWait() {
        // Prepare data
        Profile.start("ClassA", "methodA");
        
        // Typical use case
        Profile.beginWait("ClassA", "methodA");
        Profile.endWait("ClassA", "methodA");
        assertNotNull(Profile._threadActiveFrame.get(Thread.currentThread().getId()));
        
        // Edge case - non-existing frame
        Profile.endWait("ClassB", "methodB");
        assertNull(Profile._threadActiveFrame.get(Thread.currentThread().getId()));
    }
    
    @Test
    public void testUnwind() {
        // Prepare data
        Profile.start("ClassA", "methodA");
        
        // Typical use case
        Profile.unwind("ClassA", "methodA", "Exception");
        assertNotNull(Profile._threadActiveFrame.get(Thread.currentThread().getId()));
        
        // Edge case - non-existing frame
        Profile.unwind("ClassB", "methodB", "Exception");
        assertNull(Profile._threadActiveFrame.get(Thread.currentThread().getId()));
    }
    
    @Test
    public void testAlloc() {
        // Prepare data
        Profile.start("ClassA", "<init>");
        
        // Typical use case
        Profile.alloc("ClassA");
        assertNotNull(Profile._allocList.get("ClassA"));
        
        // Edge case - allocation in non-init method
        Profile.alloc("ClassA");
        assertNull(Profile._allocList.get("ClassA"));
    }
    
    @Test
    public void testRun() {
        // Prepare data
        Profile.start("ClassA", "methodA");
        Profile.end("ClassA", "methodA");
        
        // Typical use case
        Profile profile = new Profile();
        profile.run();
        assertFalse(Controller._profile);
        
        // Edge case - no frames
        Profile.clear();
        profile.run();
        assertFalse(Controller._profile);
    }
}