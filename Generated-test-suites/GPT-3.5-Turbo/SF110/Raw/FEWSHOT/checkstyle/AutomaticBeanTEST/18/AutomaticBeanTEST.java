
package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Before;
import org.junit.Test;

public class AutomaticBeanTest {
    private static final String[] ATTRIBUTE_NAMES = {"attribute1", "attribute2"};
    private static final String ATTRIBUTE_VALUE = "value";

    private AutomaticBean automaticBean;

    @Before
    public void setUp() {
        automaticBean = new AutomaticBean();
    }

    @Test
    public void testConfigure_CopyProperties() throws Exception {
        Configuration config = new ConfigurationStub(ATTRIBUTE_NAMES, ATTRIBUTE_VALUE);
        automaticBean.configure(config);

        assertEquals(ATTRIBUTE_VALUE, PropertyUtils.getProperty(automaticBean, ATTRIBUTE_NAMES[0]));
        assertEquals(ATTRIBUTE_VALUE, PropertyUtils.getProperty(automaticBean, ATTRIBUTE_NAMES[1]));
    }

    @Test
    public void testConfigure_InvalidPropertyName() {
        Configuration config = new ConfigurationStub(new String[]{"invalidAttribute"}, ATTRIBUTE_VALUE);

        assertThrows(CheckstyleException.class, () -> automaticBean.configure(config));
    }

    @Test
    public void testContextualize_CopyProperties() throws Exception {
        Context context = new ContextStub(ATTRIBUTE_NAMES, ATTRIBUTE_VALUE);
        automaticBean.contextualize(context);

        assertEquals(ATTRIBUTE_VALUE, PropertyUtils.getProperty(automaticBean, ATTRIBUTE_NAMES[0]));
        assertEquals(ATTRIBUTE_VALUE, PropertyUtils.getProperty(automaticBean, ATTRIBUTE_NAMES[1]));
    }

    private static class ConfigurationStub implements Configuration {
        private final String[] attributeNames;
        private final String attributeValue;

        ConfigurationStub(String[] attributeNames, String attributeValue) {
            this.attributeNames = attributeNames;
            this.attributeValue = attributeValue;
        }

        @Override
        public String getName() {
            return "Test Configuration";
        }

        @Override
        public String[] getAttributeNames() {
            return attributeNames;
        }

        @Override
        public String getAttribute(String key) {
            return attributeValue;
        }

        @Override
        public Configuration[] getChildren() {
            return new Configuration[0];
        }
    }

    private static class ContextStub implements Context {
        private final String[] attributeNames;
        private final Object attributeValue;

        ContextStub(String[] attributeNames, Object attributeValue) {
            this.attributeNames = attributeNames;
            this.attributeValue = attributeValue;
        }

        @Override
        public Object get(String key) {
            return attributeValue;
        }

        @Override
        public Collection<String> getAttributeNames() {
            return Arrays.asList(attributeNames);
        }
    }
}
