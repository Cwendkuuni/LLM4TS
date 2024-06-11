package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito; 

public class AutomaticBeanTest {

    @Test
    public void testConfigure() {
        Configuration config = Mockito.mock(Configuration.class);
        AutomaticBean automaticBean = new AutomaticBean();
        try {
            automaticBean.configure(config);
        } catch (CheckstyleException e) {
            fail("Exception occurred while configuring AutomaticBean");
        }
    }

    @Test
    public void testTryCopyProperty() {
        AutomaticBean automaticBean = new AutomaticBean();
        try {
            automaticBean.tryCopyProperty("moduleName", "key", "value", true);
        } catch (CheckstyleException e) {
            fail("Exception occurred while trying to copy property");
        }
    }

    @Test
    public void testContextualize() {
        Context context = Mockito.mock(Context.class);
        AutomaticBean automaticBean = new AutomaticBean();
        try {
            automaticBean.contextualize(context);
        } catch (CheckstyleException e) {
            fail("Exception occurred while contextualizing AutomaticBean");
        }
    }

    @Test
    public void testGetConfiguration() {
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(Mockito.mock(Configuration.class));
        Configuration configuration = automaticBean.getConfiguration();
        assertNotNull(configuration);
    }

    @Test
    public void testFinishLocalSetup() {
        AutomaticBean automaticBean = new AutomaticBean();
        try {
            automaticBean.finishLocalSetup();
        } catch (CheckstyleException e) {
            fail("Exception occurred while finishing local setup");
        }
    }

    @Test
    public void testSetupChild() {
        AutomaticBean automaticBean = new AutomaticBean();
        try {
            automaticBean.setupChild(Mockito.mock(Configuration.class));
        } catch (CheckstyleException e) {
            fail("Exception occurred while setting up child");
        }
    }
    
    @Test
    public void testRelaxedStringArrayConverter() {
        AutomaticBean.RelaxedStringArrayConverter converter = new AutomaticBean.RelaxedStringArrayConverter();
        
        Object result = converter.convert(String.class, "value1, value2, value3");
        assertTrue(result instanceof String[]);
        
        String[] values = (String[]) result;
        assertEquals(3, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
        assertEquals("value3", values[2]);
    }
}