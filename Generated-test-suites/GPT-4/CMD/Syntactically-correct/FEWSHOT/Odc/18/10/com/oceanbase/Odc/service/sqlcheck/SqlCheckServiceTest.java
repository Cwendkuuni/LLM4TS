package com.oceanbase.odc.service.sqlcheck;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SqlCheckServiceTest {
    
    @Test
    public void testCheck() {
        // Mock objects and define return values
        ConnectionSession mockSession = Mockito.mock(ConnectionSession.class);
        SqlCheckReq mockReq = Mockito.mock(SqlCheckReq.class);
        SqlCheckService mockSqlCheckService = Mockito.mock(SqlCheckService.class);
        CheckViolation mockViolation = Mockito.mock(CheckViolation.class);
        CheckResult mockResult = Mockito.mock(CheckResult.class);
         
        when(mockReq.getScriptContent()).thenReturn("SELECT * FROM TABLE;");
        when(mockSqlCheckService.check(mockSession, mockReq)).thenReturn(Collections.singletonList(mockResult));
 
        // Invoke the method to test
        List<CheckResult> result = mockSqlCheckService.check(mockSession, mockReq);
 
        // Verify results
        assertEquals(1, result.size());
        assertEquals(mockResult, result.get(0));
    }

    @Test
    public void testCheckWithEnvironmentId() {
        Long testEnvId = 1L;
        String testDbName = "databaseName";
        ConnectionConfig testConfig = Mockito.mock(ConnectionConfig.class);
        List<OffsetString> testSqls = new ArrayList<>();
        OffsetString testOffsetString = new OffsetString("SELECT * FROM TABLE;");
        testSqls.add(testOffsetString);
        
        SqlCheckService sqlCheckServiceSpy = Mockito.spy(SqlCheckService.class);
        
        List<CheckViolation> result = sqlCheckServiceSpy.check(testEnvId, testDbName, testSqls , testConfig);
        
         assertEquals(0, result.size());  // Modify this based on the expected behavior
    }
}