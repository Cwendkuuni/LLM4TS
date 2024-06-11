package com.puppycrawl.tools.checkstyle.api;

import org.apache.commons.beanutils.converters.*;
import java.lang.reflect.*;
import org.apache.commons.beanutils.*;
import java.beans.*;
import com.google.common.collect.*;
import java.util.*;

public class AutomaticBean implements Configurable, Contextualizable
{
    private Configuration configuration;
    
    private static BeanUtilsBean createBeanUtilsBean() {
        final ConvertUtilsBean cub = new ConvertUtilsBean();
        cub.register(new BooleanConverter(), Boolean.TYPE);
        cub.register(new BooleanConverter(), Boolean.class);
        cub.register(new ArrayConverter(boolean[].class, new BooleanConverter()), boolean[].class);
        cub.register(new ByteConverter(), Byte.TYPE);
        cub.register(new ByteConverter(), Byte.class);
        cub.register(new ArrayConverter(byte[].class, new ByteConverter()), byte[].class);
        cub.register(new CharacterConverter(), Character.TYPE);
        cub.register(new CharacterConverter(), Character.class);
        cub.register(new ArrayConverter(char[].class, new CharacterConverter()), char[].class);
        cub.register(new DoubleConverter(), Double.TYPE);
        cub.register(new DoubleConverter(), Double.class);
        cub.register(new ArrayConverter(double[].class, new DoubleConverter()), double[].class);
        cub.register(new FloatConverter(), Float.TYPE);
        cub.register(new FloatConverter(), Float.class);
        cub.register(new ArrayConverter(float[].class, new FloatConverter()), float[].class);
        cub.register(new IntegerConverter(), Integer.TYPE);
        cub.register(new IntegerConverter(), Integer.class);
        cub.register(new ArrayConverter(int[].class, new IntegerConverter()), int[].class);
        cub.register(new LongConverter(), Long.TYPE);
        cub.register(new LongConverter(), Long.class);
        cub.register(new ArrayConverter(long[].class, new LongConverter()), long[].class);
        cub.register(new ShortConverter(), Short.TYPE);
        cub.register(new ShortConverter(), Short.class);
        cub.register(new ArrayConverter(short[].class, new ShortConverter()), short[].class);
        cub.register(new RelaxedStringArrayConverter(), String[].class);
        return new BeanUtilsBean(cub, new PropertyUtilsBean());
    }
    
    @Override
    public final void configure(final Configuration config) throws CheckstyleException {
        this.configuration = config;
        final String[] attributeNames;
        final String[] attributes = attributeNames = config.getAttributeNames();
        for (final String key : attributeNames) {
            final String value = config.getAttribute(key);
            this.tryCopyProperty(config.getName(), key, value, true);
        }
        this.finishLocalSetup();
        final Configuration[] children;
        final Configuration[] childConfigs = children = config.getChildren();
        for (final Configuration childConfig : children) {
            this.setupChild(childConfig);
        }
    }
    
    private void tryCopyProperty(final String moduleName, final String key, final Object value, final boolean recheck) throws CheckstyleException {
        final BeanUtilsBean beanUtils = createBeanUtilsBean();
        try {
            if (recheck) {
                final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(this, key);
                if (descriptor == null) {
                    final String message = String.format(Locale.ROOT, "Property '%s' in module %s does not exist, please check the documentation", key, moduleName);
                    throw new CheckstyleException(message);
                }
            }
            beanUtils.copyProperty(this, key, value);
        }
        catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException ex5) {
            final ReflectiveOperationException ex3;
            final ReflectiveOperationException ex = ex3;
            final String message = String.format(Locale.ROOT, "Cannot set property '%s' to '%s' in module %s", key, value, moduleName);
            throw new CheckstyleException(message, ex);
        }
        catch (IllegalArgumentException | ConversionException ex6) {
            final RuntimeException ex4;
            final RuntimeException ex2 = ex4;
            final String message = String.format(Locale.ROOT, "illegal value '%s' for property '%s' of module %s", value, key, moduleName);
            throw new CheckstyleException(message, ex2);
        }
    }
    
    @Override
    public final void contextualize(final Context context) throws CheckstyleException {
        final Collection<String> attributes = context.getAttributeNames();
        for (final String key : attributes) {
            final Object value = context.get(key);
            this.tryCopyProperty(this.getClass().getName(), key, value, false);
        }
    }
    
    protected final Configuration getConfiguration() {
        return this.configuration;
    }
    
    protected void finishLocalSetup() throws CheckstyleException {
    }
    
    protected void setupChild(final Configuration childConf) throws CheckstyleException {
        if (childConf != null) {
            throw new CheckstyleException(childConf.getName() + " is not allowed as a child in " + this.getConfiguration().getName());
        }
    }
    
    private static class RelaxedStringArrayConverter implements Converter
    {
        @Override
        public Object convert(final Class type, final Object value) {
            final StringTokenizer tokenizer = new StringTokenizer(value.toString().trim(), ",");
            final List<String> result = (List<String>)Lists.newArrayList();
            while (tokenizer.hasMoreTokens()) {
                final String token = tokenizer.nextToken();
                result.add(token.trim());
            }
            return result.toArray(new String[result.size()]);
        }
    }
}
