package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class SqlCheckServiceTest {

    @Test
    public void testCheckWithConnectionSession() {
        // Create a mock ConnectionSession
        ConnectionSession session = new ConnectionSession();
        
        // Create a mock SqlCheckReq
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM employees;");
        
        // Test check method with ConnectionSession
        SqlCheckService sqlCheckService = new SqlCheckService();
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertNotNull(checkResults);
    }

    @Test
    public void testCheckWithConnectionConfig() {
        // Create mock data
        Long environmentId = 123L;
        String databaseName = "test";
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT * FROM employees;", 0, 0));
        ConnectionConfig config = new ConnectionConfig();
        
        // Test check method with ConnectionConfig
        SqlCheckService sqlCheckService = new SqlCheckService();
        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertNotNull(checkViolations);
    }

}