
package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;

    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
    }

    @Test
    public void testCheckWithConnectionSession() {
        // Prepare test data
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM tablename");

        // Perform the test
        List<CheckResult> checkResults = sqlCheckService.check(session, req);

        // Validate the results
        assertNotNull(checkResults);
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheckWithEnvironmentIdAndDatabaseName() {
        // Prepare test data
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString(0, "SELECT * FROM tablename"));
        ConnectionConfig config = new ConnectionConfig();

        // Perform the test
        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);

        // Validate the results
        assertNotNull(checkViolations);
        assertEquals(0, checkViolations.size());
    }

    // Add more test cases to cover additional scenarios for the methods in SqlCheckService class

}
