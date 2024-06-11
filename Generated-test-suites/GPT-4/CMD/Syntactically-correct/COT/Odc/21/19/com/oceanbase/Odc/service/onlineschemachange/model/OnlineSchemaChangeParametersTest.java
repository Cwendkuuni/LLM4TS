package com.oceanbase.odc.service.onlineschemachange.model;
import org.junit.Assert;
import org.junit.Test;

public class OnlineSchemaChangeParametersTest {
    
    @Test
    public void testIsContinueOnError() {
        //...write your test logic here...
    }
    
    @Test
    public void testGenerateSubTaskParameters() {
        //...write your test logic here...
    }

    @Test
    public void testIsContinueOnError_whenErrorStrategyNotSet() {
        //...write your test logic here...
    }

    @Test
    public void testGenerateSubTaskParameters_whenConnectionConfigIsNull() {
        //...write your test logic here...
    }
    
    @Test
    public void testGenerateSubTaskParameters_whenSchemaIsNull() {
        //...write your test logic here...
    }
    
    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters_whenSqlContentIsEmpty() {
        //...write your test logic here...
    }
    
    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters_whenExceptionIsThrown() {
        //...write your test logic here...
    }
}