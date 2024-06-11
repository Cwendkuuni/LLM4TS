import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.Vector;

import static org.junit.Assert.*;

public class BatchDriverTest {
    private BatchDriver batchDriver;
    
    @Before
    public void setUp() {
        batchDriver = new BatchDriver("newzini", "batchfile");
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(batchDriver);
    }
    
    @Test
    public void testGetGroups() {
        String[] groups = batchDriver.getGroups();
        
        assertNotNull(groups);
        assertEquals(0, groups.length);
        
        batchDriver.BatchgroupsVector.add(new String[]{"group1", "description1"});
        batchDriver.BatchgroupsVector.add(new String[]{"group2", "description2"});
        
        groups = batchDriver.getGroups();
        
        assertNotNull(groups);
        assertEquals(2, groups.length);
        assertEquals("group1", groups[0]);
        assertEquals("group2", groups[1]);
    }
    
    @Test
    public void testGetBatchJob() {
        BatchJob batchJob = batchDriver.getBatchJob("group1");
        
        assertNull(batchJob);
        
        batchDriver.BatchgroupsHash.put("group1", "directory1");
        batchJob = batchDriver.getBatchJob("group1");
        
        assertNotNull(batchJob);
        assertEquals("server", getField(batchJob, "server"));
        assertEquals("group1", getField(batchJob, "group"));
        assertEquals("directory1", getField(batchJob, "directory"));
        assertEquals(0, getField(batchJob, "firstid"));
    }
    
    @Test
    public void testGetBatchJob_ExceptionThrown() {
        BatchJob batchJob = batchDriver.getBatchJob("group1");
        
        assertNull(batchJob);
        
        batchDriver.OptionsHash.put("server", "server");
        batchDriver.OptionsHash.put("threads", "1");
        batchDriver.OptionsHash.put("timeout", "5000");
        
        batchDriver.BatchgroupsHash.put("group1", "directory1");
        batchDriver.NewsgroupsHash.put("group1", "1");
        batchDriver.BatchgroupsVector.add(new String[]{"group1", "description1"});
        
        batchJob = batchDriver.getBatchJob("group1");
        
        assertNull(batchJob);
    }
    
    @Test
    public void testGetSubjectLines() {
        String[] subjectLines = batchDriver.getSubjectLines();
        
        assertNull(subjectLines);
        
        batchDriver.subjectlines = new String[]{"subject1", "subject2"};
        
        subjectLines = batchDriver.getSubjectLines();
        
        assertNotNull(subjectLines);
        assertEquals(2, subjectLines.length);
        assertEquals("subject1", subjectLines[0]);
        assertEquals("subject2", subjectLines[1]);
    }
    
    private Object getField(Object object, String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}