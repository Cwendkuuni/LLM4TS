package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ProfileTest {
    @Before
    public void setUp() {
        Profile.initProfiler();
    }
    
    @Test
    public void testInitProfiler() {
        // test initialization of profiler
    }
    
    @Test
    public void testInit() {
        // test initialization of profile variables
    }
    
    @Test
    public void testClear() {
        // test clearing profile variables
    }
    
    @Test
    public void testStart() {
        // test starting profile
    }
    
    @Test
    public void testStop() {
        // test stopping profile
    }
    
    @Test
    public void testSetFileName() {
        // test setting filename for profile output
    }
    
    @Test
    public void testShutdown() {
        // test shutting down profile
    }
    
    @Test
    public void testThreads() {
        // test retrieving threads
    }
    
    @Test
    public void testInteractions() {
        // test retrieving interactions for a specific thread
    }
    
    @Test
    public void testFrameList() {
        // test retrieving frame list
    }
    
    @Test
    public void testAllocations() {
        // test retrieving allocations
    }
    
    @Test
    public void testGetThreadTotalTime() {
        // test getting total time for a specific thread
    }
    
    @Test
    public void testSortFrameList() {
        // test sorting frame list
    }
    
    @Test
    public void testStartMethod() {
        // test starting a method frame
    }
    
    @Test
    public void testEndMethod() {
        // test ending a method frame
    }
    
    @Test
    public void testBeginWait() {
        // test beginning wait for a method
    }
    
    @Test
    public void testEndWait() {
        // test ending wait for a method
    }
    
    @Test
    public void testUnwind() {
        // test unwinding method frames
    }
    
    @Test
    public void testAlloc() {
        // test allocation of objects
    }
    
    @Test
    public void testRun() {
        // test running the Profile class
    }
}