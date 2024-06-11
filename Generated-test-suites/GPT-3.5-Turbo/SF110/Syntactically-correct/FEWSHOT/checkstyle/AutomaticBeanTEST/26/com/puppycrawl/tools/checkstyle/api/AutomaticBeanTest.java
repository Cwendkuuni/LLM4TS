package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

public class AutomaticBeanTest {
    private AutomaticBean automaticBean;

    @Before
    public void setUp() {
        automaticBean = new AutomaticBean();
    }

    @Test
    public void testConfigure() {
        Configuration config = new ConfigurationStub();
        automaticBean.configure(config);
        assertNotNull(automaticBean.getConfiguration());
    }

    @Test
    public void testTryCopyProperty() throws CheckstyleException {
        String moduleName = "moduleName";
        
        // Existing property
        automaticBean.tryCopyProperty(moduleName, "configuration", new ConfigurationStub(), true);
        assertNotNull(automaticBean.getConfiguration());
        
        // Non-existing property
        try {
            automaticBean.tryCopyProperty(moduleName, "nonExistingProperty", null, true);
        } catch (CheckstyleException e) {
            assertEquals(String.format(Locale.ROOT, "Property 'nonExistingProperty' in module %s does not exist, please check the documentation", moduleName), e.getMessage());
        }
    }

    @Test
    public void testContextualize() throws CheckstyleException {
        Context context = new ContextStub();
        automaticBean.contextualize(context);
        assertNull(automaticBean.getConfiguration());
    }

    @Test
    public void testFinishLocalSetup() throws CheckstyleException {
        automaticBean.finishLocalSetup();
        // No assertions needed, method does not have any logic
    }

    @Test
    public void testSetupChild() throws CheckstyleException {
        Configuration childConf = new ConfigurationStub();
        try {
            automaticBean.setupChild(childConf);
        } catch (CheckstyleException e) {
            assertEquals(childConf.getName() + " is not allowed as a child in " + automaticBean.getConfiguration().getName(), e.getMessage());
        }
        
        // Null child configuration
        try {
            automaticBean.setupChild(null);
        } catch (CheckstyleException e) {
            assertNotNull(e);
        }
    }

    private class ConfigurationStub implements Configuration {
        private String[] attributeNames;
        private Configuration[] children;
        
        @Override
        public String getName() {
            return "Test Configuration";
        }

        @Override
        public String[] getAttributeNames() {
            return attributeNames;
        }

        @Override
        public String getAttribute(String attributeName) {
            return "";
        }

        @Override
        public Configuration[] getChildren() {
            return children;
        }
    }

    private class ContextStub implements Context {
        @Override
        public Collection<String> getAttributeNames() {
            return null;
        }

        @Override
        public <T> T get(String attributeName) {
            return null;
        }
    }
}