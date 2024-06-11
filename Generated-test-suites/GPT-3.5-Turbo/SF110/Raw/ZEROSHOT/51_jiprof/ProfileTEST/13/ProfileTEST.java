import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.Comparator;
import java.util.List;

public class ProfileTest {
    
    @Test
    public void testInitProfiler() {
        Profile.initProfiler();
        // Test code here
    }
    
    @Test
    public void testInit() {
        Profile.init();
        // Test code here
    }
    
    @Test
    public void testClear() {
        Profile.clear();
        // Test code here
    }
    
    @Test
    public void testStart() {
        Profile.start();
        // Test code here
    }
    
    @Test
    public void testStop() {
        Profile.stop();
        // Test code here
    }
    
    @Test
    public void testSetFileName() {
        Profile.setFileName("testfile.txt");
        // Test code here
    }
    
    @Test
    public void testShutdown() {
        Profile.shutdown();
        // Test code here
    }
    
    @Test
    public void testThreads() {
        Iterable<Long> threads = Profile.threads();
        // Test code here
    }
    
    @Test
    public void testInteractions() {
        Iterable<Frame> interactions = Profile.interactions(12345L);
        // Test code here
    }
    
    @Test
    public void testFrameList() {
        Iterable<Frame> frameList = Profile.frameList();
        // Test code here
    }
    
    @Test
    public void testAllocations() {
        Iterable<ClassAllocation> allocations = Profile.allocations();
        // Test code here
    }
    
    @Test
    public void testGetThreadTotalTime() {
        long totalTime = Profile.getThreadTotalTime(12345L);
        // Test code here
    }
    
    @Test
    public void testSortFrameList() {
        Comparator<Frame> comp = new Comparator<Frame>() {
            public int compare(Frame f1, Frame f2) {
                return f1.getEndTime() - f2.getEndTime();
            }
        };
        Profile.sortFrameList(comp);
        // Test code here
    }
    
    @Test
    public void testStartMethod() {
        Profile.start("TestClass", "testMethod");
        // Test code here
    }
    
    @Test
    public void testEndMethod() {
        Profile.end("TestClass", "testMethod");
        // Test code here
    }
    
    @Test
    public void testBeginWait() {
        Profile.beginWait("TestClass", "testMethod");
        // Test code here
    }
    
    @Test
    public void testEndWait() {
        Profile.endWait("TestClass", "testMethod");
        // Test code here
    }
    
    @Test
    public void testUnwind() {
        Profile.unwind("TestClass", "testMethod", "Exception");
        // Test code here
    }
    
    @Test
    public void testAlloc() {
        Profile.alloc("TestClass");
        // Test code here
    }
    
    @Test
    public void testRun() {
        Profile profile = new Profile();
        profile.run();
        // Test code here
    }
}