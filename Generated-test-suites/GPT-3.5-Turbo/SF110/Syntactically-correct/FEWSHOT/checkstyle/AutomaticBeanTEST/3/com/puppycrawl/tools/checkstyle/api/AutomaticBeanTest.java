package com.puppycrawl.tools.checkstyle.api;

import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AutomaticBeanTest {

    @Mock
    private Configuration configuration;

    @InjectMocks
    private AutomaticBean automaticBean;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConfigure() throws CheckstyleException {
        // Setup
        Configuration childConfig1 = new Configuration();
        childConfig1.setName("ChildConfig1");
        childConfig1.setAttribute("key1", "value1");

        Configuration childConfig2 = new Configuration();
        childConfig2.setName("ChildConfig2");
        childConfig2.setAttribute("key2", "value2");

        Configuration[] childConfigs = { childConfig1, childConfig2 };
        configuration.setChildren(childConfigs);

        // Execute
        automaticBean.configure(configuration);

        // Verify
        assertNotNull(automaticBean.getConfiguration());

        Collection<String> attributeNames = configuration.getAttributeNames();
        for (String attributeName : attributeNames) {
            String attributeValue = configuration.getAttribute(attributeName);
            verifyCopyProperty(attributeName, attributeValue, true);
        }

        Configuration[] children = configuration.getChildren();
        for (Configuration child : children) {
            verifySetupChild(child);
        }
    }

    @Test
    public void testContextualize() throws CheckstyleException {
        // Setup
        Context context = new Context();
        context.setAttribute("key1", "value1");
        context.setAttribute("key2", "value2");

        // Execute
        automaticBean.contextualize(context);

        // Verify
        assertNotNull(automaticBean.getConfiguration());

        Collection<String> attributeNames = context.getAttributeNames();
        for (String attributeName : attributeNames) {
            Object attributeValue = context.get(attributeName);
            verifyCopyProperty(AutomaticBean.class.getName(), attributeName, attributeValue, false);
        }
    }

    private void verifyCopyProperty(String moduleName, String key, Object value, boolean recheck) throws CheckstyleException {
        ConvertUtilsBean beanUtils = new ConvertUtilsBean();
        try {
            if (recheck) {
                PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(automaticBean, key);
                if (descriptor == null) {
                    String message = String.format(Locale.ROOT, "Property '%s' in module %s does not exist, please check the documentation", key, moduleName);
                    throw new CheckstyleException(message);
                }
            }
            beanUtils.copyProperty(automaticBean, key, value);
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException ex) {
            String message = String.format(Locale.ROOT, "Cannot set property '%s' to '%s' in module %s", key, value, moduleName);
            throw new CheckstyleException(message, ex);
        } catch (IllegalArgumentException | ConversionException ex) {
            String message = String.format(Locale.ROOT, "illegal value '%s' for property '%s' of module %s", value, key, moduleName);
            throw new CheckstyleException(message, ex);
        }
    }

    private void verifySetupChild(Configuration childConf) throws CheckstyleException {
        if (childConf != null) {
            throw new CheckstyleException(childConf.getName() + " is not allowed as a child in " + automaticBean.getConfiguration().getName());
        }
    }

    private class RelaxedStringArrayConverterMock implements Converter {
        @Override
        public Object convert(Class type, Object value) {
            StringTokenizer tokenizer = new StringTokenizer(value.toString().trim(), ",");
            List<String> result = Lists.newArrayList();
            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();
                result.add(token.trim());
            }
            return result.toArray(new String[result.size()]);
        }
    }
}