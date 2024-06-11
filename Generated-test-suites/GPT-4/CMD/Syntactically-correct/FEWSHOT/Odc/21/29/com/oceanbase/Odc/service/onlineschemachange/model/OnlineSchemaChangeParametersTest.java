
package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.Before;

import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;


public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters parameters;
    ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        parameters = new OnlineSchemaChangeParameters();
        connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("Oracle");
    }

    @Test
    public void testIsContinueOnError() {
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        parameters.setSqlContent("SELECT * FROM users;");
        parameters.setDelimiter(";");
        List<OnlineSchemaChangeScheduleTaskParameters> params = parameters.generateSubTaskParameters(connectionConfig, "testscheme");
        assertNotNull(params);
        assertTrue(params.size() > 0);
    }

    @Test
    public void testLockTableTimeoutSeconds() {
        final Integer timeout = 120;
        parameters.setLockTableTimeOutSeconds(timeout);
        assertEquals(timeout, parameters.getLockTableTimeOutSeconds());
    }

    @Test
    public void testSwapTableNameRetryTimes() {
        final Integer retryTimes = 5;
        parameters.setSwapTableNameRetryTimes(retryTimes);
        assertEquals(retryTimes, parameters.getSwapTableNameRetryTimes());
    }
    
    @Test
    public void testSqlContent(){
        final String sqlContent = "SELECT * FROM catalogue";
        parameters.setSqlContent(sqlContent);
        assertEquals(sqlContent, parameters.getSqlContent());
    }

    @Test
    public void testDelimiter(){
        final String delimiter = ":";
        parameters.setDelimiter(delimiter);
        assertEquals(delimiter, parameters.getDelimiter());
    }

    //Perform other tests if needed
}
