package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.RelaxedStringArrayConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.junit.Before;
import org.junit.Test;

public class AutomaticBeanTest {
    private static final String KEY_1 = "key1";
    private static final String KEY_2 = "key2";
    private static final String KEY_3 = "key3";
    private static final String KEY_4 = "key4";
    
    private AutomaticBean automaticBean;
    
    @Before
    public void setUp() {
        automaticBean = new AutomaticBean();
    }
    
    @Test
    public void testConfigure() throws CheckstyleException {    	
        Configuration configuration = new ConfigurationMock();
        
        automaticBean.configure(configuration);
        
        assertEquals(configuration, automaticBean.getConfiguration());
    }
    
    @Test
    public void testConfigure_PropertyDoesNotExist() {
        Configuration configuration = new ConfigurationMock();
        configuration.setAttribute(KEY_1, "InvalidProperty");
        
        try {
            automaticBean.configure(configuration);
        } catch (CheckstyleException e) {
            assertEquals(String.format(Locale.ROOT, "Property '%s' in module %s does not exist, please check the documentation", KEY_1, configuration.getName()), e.getMessage());
        }
    }
    
    @Test
    public void testConfigure_ConversionException() {
        Configuration configuration = new ConfigurationMock();
        configuration.setAttribute(KEY_2, "InvalidValue");
        
        try {
            automaticBean.configure(configuration);
        } catch (CheckstyleException e) {
            assertEquals(String.format(Locale.ROOT, "illegal value '%s' for property '%s' of module %s", "InvalidValue", KEY_2, configuration.getName()), e.getMessage());
        }
    }
    
    @Test
    public void testConfigure_CheckstyleException() {
        Configuration configuration = new ConfigurationMock();
        configuration.setAttribute(KEY_3, "CheckstyleException");
        
        try {
            automaticBean.configure(configuration);
        } catch (CheckstyleException e) {
            assertTrue(e.getCause() instanceof ReflectiveOperationException);
            assertEquals(String.format(Locale.ROOT, "Cannot set property '%s' to '%s' in module %s", KEY_3, "CheckstyleException", configuration.getName()), e.getMessage());
        }
    }
    
    @Test
    public void testConfigure_NestedConfiguration() throws CheckstyleException {    	
        Configuration nestedConfiguration = new ConfigurationMock();
        Configuration configuration = new ConfigurationMock(Arrays.asList(nestedConfiguration));
        
        automaticBean.configure(configuration);
        
        assertEquals(configuration, automaticBean.getConfiguration());
    }
    
    @Test
    public void testContextualize() throws CheckstyleException { 
        Context context = new ContextMock();
        
        automaticBean.contextualize(context);
        
        assertNull(automaticBean.getConfiguration());
    }
    
    @Test
    public void testContextualize_PropertyDoesNotExist() {
        Context context = new ContextMock();
        context.setAttribute(KEY_1, "InvalidProperty");
        
        try {
            automaticBean.contextualize(context);
        } catch (CheckstyleException e) {
            assertEquals(String.format(Locale.ROOT, "Property '%s' in module %s does not exist, please check the documentation", KEY_1, automaticBean.getClass().getName()), e.getMessage());
        }
    }
    
    @Test
    public void testContextualize_ConversionException() {
        Context context = new ContextMock();
        context.setAttribute(KEY_2, "InvalidValue");
        
        try {
            automaticBean.contextualize(context);
        } catch (CheckstyleException e) {
            assertEquals(String.format(Locale.ROOT, "illegal value '%s' for property '%s' of module %s", "InvalidValue", KEY_2, automaticBean.getClass().getName()), e.getMessage());
        }
    }
    
    @Test
    public void testContextualize_NestedConfiguration() {
        Context context = new ContextMock();
        context.setAttribute(KEY_3, "Nested Configuration");
        
        try {
            automaticBean.contextualize(context);
        } catch (CheckstyleException e) {
            assertTrue(e.getCause() instanceof ReflectiveOperationException);
            assertEquals(String.format(Locale.ROOT, "Cannot set property '%s' to '%s' in module %s", KEY_3, "Nested Configuration", automaticBean.getClass().getName()), e.getMessage());
        }
    }
    
    @Test
    public void testFinishLocalSetup() throws CheckstyleException {
        automaticBean.finishLocalSetup();
    }
    
    @Test
    public void testSetupChild_NullChild() {
        try {
            automaticBean.setupChild(null);
        } catch(CheckstyleException e) {
            assertEquals("null is not allowed as a child in null", e.getMessage());
        }
    }
    
    private class ConfigurationMock implements Configuration {
        private String[] attributeNames;
        private List<Configuration> children;
        
        public ConfigurationMock() {
            this.attributeNames = new String[] {KEY_1, KEY_2, KEY_3, KEY_4};
            this.children = Collections.emptyList();
        }
        
        public ConfigurationMock(List<Configuration> children) {
            this.attributeNames = new String[] {KEY_1, KEY_2, KEY_3, KEY_4};
            this.children = children;
        }
        
        @Override
        public String getName() {
            return "Mock Configuration";
        }

        @Override
        public String getAttribute(String attributeName) {
            if(attributeName.equals(KEY_1))
                return "ValidProperty";
            if(attributeName.equals(KEY_2))
                return "True";
            if(attributeName.equals(KEY_3))
                return "ValidValue";
            return null;
        }

        @Override
        public String[] getAttributeNames() {
            return attributeNames;
        }

        @Override
        public Configuration[] getChildren() {
            return children.toArray(new Configuration[children.size()]);
        }   	
    }
    
    private class ContextMock implements Context {
    	
    	@Override
    	public Object get(String name) {
    		if(name.equals(KEY_1))
    			return "ValidProperty";
    		if(name.equals(KEY_2))
    			return "True";
    		if(name.equals(KEY_3))
    			return "ValidValue";
    		return null;
    	}
    	
    	@Override
    	public Collection<String> getAttributeNames() {    		
    		return Arrays.asList(KEY_1, KEY_2, KEY_3, KEY_4);
    	}
    }
}