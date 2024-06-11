package com.netflix.conductor.core.utils;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.model.WorkflowModel;

import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private WorkflowModel workflowModel;
    private Map<String, Object> inputParams;

    @Before
    public void setup() {
        parametersUtils = new ParametersUtils(objectMapper);
        workflowModel = new WorkflowModel();
        inputParams = new HashMap<>();
    }

    @Test
    public void getTaskInputTest() {
        //  mock up workflow model and task id then call getTaskInput, 
        //  check the returned object is correct or not
    }

    @Test
    public void getTaskInputV2Test() {
        //  mock up parameters then call getTaskInputV2, 
        //  check the returned object is correct or not
    }

    @Test
    public void cloneTest() {
        //  mock up a map then call clone, check the returned map 
        //  is equal to the original one or not
    }

    @Test
    public void replaceTest() {
        //  mock up a map and a string in json format 
        //  then call replace, check the returned object is correct or not
    }

    @Test
    public void replaceStringTest() {
        //  mock up a string then call replace, 
        //  check the returned object is the same as expected or not
    }

    @Test
    public void getTaskInputV1Test() {
        //  mock up a workflow model and input params 
        //  then call getTaskInputV1, check the returned object is equal to 
        //  the input params or not
    }

    @Test
    public void getWorkflowInputTest() {
        //  mock up parameters then call getWorkflowInput, 
        //  check returned object is equal to input params or not
    }
}