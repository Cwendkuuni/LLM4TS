package com.oceanbase.odc.service.onlineschemachange;

import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest
@ContextConfiguration(classes = OscService.class)
public class OscServiceTest {

    @Autowired
    private OscService oscService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        OscLockDatabaseUserInfo oscLockDatabaseUserInfo = oscService.getOscDatabaseInfo(id);
        assertNotNull(oscLockDatabaseUserInfo);
        // Add more assertions based on expected behavior
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        OscSwapTableVO oscSwapTableVO = oscService.swapTable(scheduleTaskId);
        assertNotNull(oscSwapTableVO);
        // Add more assertions based on expected behavior
    }

    // Add more test cases for edge cases and exception scenarios

}

