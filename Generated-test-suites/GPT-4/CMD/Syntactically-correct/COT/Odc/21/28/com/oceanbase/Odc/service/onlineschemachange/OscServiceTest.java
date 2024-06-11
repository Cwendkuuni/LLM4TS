package com.oceanbase.odc.service.onlineschemachange;
import org.junit.Assert;
import org.junit.Test;

public class OscServiceTest {
    
    @Test
    public void testGetOscDatabaseInfo() {
        OscService service = new OscService();
        OscLockDatabaseUserInfo result = service.getOscDatabaseInfo(1L);
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testSwapTable() {
        OscService service = new OscService();
        OscSwapTableVO result = service.swapTable(1L);
        Assert.assertNotNull(result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetOscDatabaseInfoWithNullId() {
        OscService service = new OscService();
        service.getOscDatabaseInfo(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSwapTableWithNullId() {
        OscService service = new OscService();
        service.swapTable(null);
    }

    @Test
    public void testGetOscDatabaseInfoWithNonExistentId() {
        OscService service = new OscService();
        OscLockDatabaseUserInfo result = service.getOscDatabaseInfo(999999L);
        Assert.assertNotNull(result);
    }
}