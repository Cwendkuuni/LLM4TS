
package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeParametersTest {

    @Mock
    ConnectionConfig connectionConfig;

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params.isContinueOnError());
        params.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(params.isContinueOnError());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWithUnexpectedException() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.generateSubTaskParameters(connectionConfig, "");
        verify(connectionConfig).getDialectType();
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();

        // test for empty sqlContent
        params.setSqlContent("");
        List<OnlineSchemaChangeScheduleTaskParameters> taskParametersEmptySql = params.generateSubTaskParameters(connectionConfig, "");
        assertTrue(taskParametersEmptySql.isEmpty());

        // test for non-empty sqlContent
        params.setSqlContent("create table test;");
        List<OnlineSchemaChangeScheduleTaskParameters> taskParametersNonemptySql = params.generateSubTaskParameters(connectionConfig, "");
        assertFalse(taskParametersNonemptySql.isEmpty());
    }

    @Test
    public void testGenerateSubTaskParametersWithDelimiter() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlContent("create table test1;create table test2;");
        params.setDelimiter(";");
        
        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = params.generateSubTaskParameters(connectionConfig, "");
        assertEquals(2, taskParameters.size());
    }

    @Test
    public void testGenerateSubTaskParametersWithoutDelimiter() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlContent("create table test1 create table test2");
        params.setDelimiter(" ");
        
        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = params.generateSubTaskParameters(connectionConfig, "");
        assertEquals(2, taskParameters.size());
    }
}
