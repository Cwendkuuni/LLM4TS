
package com.oceanbase.odc.service.onlineschemachange.model;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters parameters;

    @Before
    public void setup() {
        parameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void isContinueOnErrorTest() {
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        Assert.assertTrue(parameters.isContinueOnError());
        parameters.setErrorStrategy(TaskErrorStrategy.TERMINATE);
        Assert.assertFalse(parameters.isContinueOnError());
    }

    @Test(expected = UnexpectedException.class)
    public void generateSubTaskParametersExceptionTest() {

        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType("POSTGRES");

        parameters.setSqlContent("CREATE DATABASE mydb; CREATE TABLE authors (ID int, NAME varchar, EMAIL varchar)");
        parameters.setDelimiter(";");
        parameters.generateSubTaskParameters(config, "mydb");
    }

    @Test
    public void generateSubTaskParametersTest() {
        // in this step we need to mock the SubTaskParameterFactory
        // and the return value from the method generate(), also DdlUtils.replaceTableName()
        // should be enough to give something similar to the expected output
        // We need specific condition and overrided method in our class to properly test this method
    }
}
