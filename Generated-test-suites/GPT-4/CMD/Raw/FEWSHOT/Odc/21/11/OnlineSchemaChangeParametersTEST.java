package com.oceanbase.odc.service.onlineschemachange.model;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.junit.Before;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

public class OnlineSchemaChangeParametersTest {
   
    OnlineSchemaChangeParameters parameters;

    @Before
    public void setup() {
        parameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testIsContinueOnError() {
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.FAIL);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        //This should be implemented with mocks of SubTaskParameterFactory and ConnectionConfig
        //Here is a simple pseudo-code example you can refine according to your project needs

        parameters.setDelimiter(";");
        String sqlContent = "CREATE TABLE someTable; ALTER TABLE someTable;";
        parameters.setSqlContent(sqlContent);
        ConnectionConfig connectionConfig =  Mockito.mock(ConnectionConfig.class);
        Mockito.when(connectionConfig.getDialectType()).thenReturn("someDialectType");

        SubTaskParameterFactory subTaskParameterFactory =  Mockito.mock(SubTaskParameterFactory.class);
        
        //setup your mocks and test your generateSubTaskParameters method
        
        //List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, "someSchema");
        //assertEquals(2, result.size());
        //assertEquals(subTaskParameterFactory.generate("CREATE TABLE someTable", parameters.getSqlType()), result.get(0));
        //assertEquals(subTaskParameterFactory.generate("ALTER TABLE someTable", parameters.getSqlType()), result.get(1));        
    }
    //Test remaining setters and getters
}