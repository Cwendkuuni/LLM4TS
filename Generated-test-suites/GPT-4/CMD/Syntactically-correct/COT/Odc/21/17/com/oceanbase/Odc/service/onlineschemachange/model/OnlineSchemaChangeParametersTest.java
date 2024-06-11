package com.oceanbase.odc.service.onlineschemachange.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

import java.util.ArrayList;

public class OnlineSchemaChangeParametersTest {
    private OnlineSchemaChangeParameters parameters;

    @Before
    public void setup(){
        parameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testIsContinueOnErrorTrue(){
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());
    }

    @Test
    public void testIsContinueOnErrorFalse(){
        parameters.setErrorStrategy(TaskErrorStrategy.ROLLBACK);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters(){
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setSqlContent("CREATE TABLE demo (id INT PRIMARY KEY, name VARCHAR(20));");
        parameters.setDelimiter(";");
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType("Oracle");
        try{
            assertNotNull(parameters.generateSubTaskParameters(config, "testSchema"));
        } catch (Exception e) {
            fail("Exception not expected here");
        }
    }

    @Test(expected = RuntimeException.class)
    public void testGenerateSubTaskParametersException(){
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setSqlContent("CREATE TABLE demo (id INT PRIMARY KEY, name VARCHAR(20));");
        parameters.setDelimiter(";");
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType("Invalid");
        
        parameters.generateSubTaskParameters(config, "testSchema");

        // Exception should be here due to the Invalid Dialect Type
    }
}
