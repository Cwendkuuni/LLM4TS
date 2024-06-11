package Newzgrabber;

import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.util.*;

public class BatchDriverTest {

    @Test
    public void testGetGroups() {
        BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
        String[] expectedGroups = {"group1", "group2", "group3"};
        String[] actualGroups = batchDriver.getGroups();
        assertArrayEquals(expectedGroups, actualGroups);
    }

    @Test
    public void testGetBatchJob() {
        BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
        BatchJob expectedBatchJob = new BatchJob("server", "group", "directory", 0, 10);
        BatchJob actualBatchJob = batchDriver.getBatchJob("group");
        assertEquals(expectedBatchJob.getServer(), actualBatchJob.getServer());
        assertEquals(expectedBatchJob.getGroup(), actualBatchJob.getGroup());
        assertEquals(expectedBatchJob.getDirectory(), actualBatchJob.getDirectory());
        assertEquals(expectedBatchJob.getFirstId(), actualBatchJob.getFirstId());
        assertEquals(expectedBatchJob.getLastId(), actualBatchJob.getLastId());
    }

    @Test
    public void testGetSubjectLines() {
        BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
        String[] expectedSubjectLines = {"subject1", "subject2", "subject3"};
        String[] actualSubjectLines = batchDriver.getSubjectLines();
        assertArrayEquals(expectedSubjectLines, actualSubjectLines);
    }
}