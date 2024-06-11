package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import com.mentorgen.tools.profile.output.*;


public class ProfileTest {

    @Test
    public void testInitProfiler() {
        Profile.initProfiler();
        // assert statements
    }
    
    @Test
    public void testInit() {
        Profile.init();
        // assert statements
    }
    
    @Test
    public void testClear() {
        Profile.clear();
        // assert statements
    }
    
    @Test
    public void testStart() {
        Profile.start();
        // assert statements
    }
    
    @Test
    public void testStop() {
        Profile.stop();
        // assert statements
    }

    @Test
    public void testSetFileName() {
        Profile.setFileName("testFile");
        // assert statements
    }
    
    @Test
    public void testShutdown() {
        Profile.shutdown();
        // assert statements
    }
    
    @Test
    public void testThreads() {
        Iterable<Long> threads = Profile.threads();
        // assert statements
    }
    
    @Test
    public void testInteractions() {
        Iterable<Frame> interactions = Profile.interactions(123L);
        // assert statements
    }
    
    @Test
    public void testFrameList() {
        Iterable<Frame> frameList = Profile.frameList();
        // assert statements
    }
    
    @Test
    public void testAllocations() {
        Iterable<ClassAllocation> allocations = Profile.allocations();
        // assert statements
    }

    @Test
    public void testGetThreadTotalTime() {
        long totalTime = Profile.getThreadTotalTime(123L);
        // assert statements
    }

    @Test
    public void testSortFrameList() {
        Comparator<Frame> comp = new Comparator<Frame>() {
            public int compare(Frame f1, Frame f2) {
                // comparison logic
                return 0;
            }
        };
        
        Profile.sortFrameList(comp);
        // assert statements
    }
    
    @Test
    public void testStartWithClassNameAndMethodName() {
        Profile.start("ClassName", "MethodName");
        // assert statements
    }
    
    @Test
    public void testEndWithClassNameAndMethodName() {
        Profile.end("ClassName", "MethodName");
        // assert statements
    }
    
    @Test
    public void testBeginWaitWithClassNameAndMethodName() {
        Profile.beginWait("ClassName", "MethodName");
        // assert statements
    }
    
    @Test
    public void testEndWaitWithClassNameAndMethodName() {
        Profile.endWait("ClassName", "MethodName");
        // assert statements
    }
    
    @Test
    public void testUnwindWithClassNameAndMethodNameAndException() {
        Profile.unwind("ClassName", "MethodName", "Exception");
        // assert statements
    }
    
    @Test
    public void testAllocWithClassName() {
        Profile.alloc("ClassName");
        // assert statements
    }
    
    @Test
    public void testRun() {
        Profile profile = new Profile();
        profile.run();
        // assert statements
    }
}