package com.example; // replace with the correct package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ValueInstantiatorTest {

    private static final String VALUE_TYPE = "java.lang.String";

    private ValueInstantiator valueInstantiator;
    private Object instance;
    private DeserializationContext context;

    public ValueInstantiatorTest(boolean canCreateUsingDefault, boolean canCreateFromObjectWith) {
        this.valueInstantiator = new ValueInstantiator.Base(ValueInstantiatorTest.class) {
            @Override
            public boolean canCreateUsingDefault() {
                return canCreateUsingDefault;
            }

            @Override
            public boolean canCreateFromObjectWith() {
                return canCreateFromObjectWith;
            }
        };
        this.context = mock(DeserializationContext.class);
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {true, false},
                {false, true},
                {false, false}
        });
    }

    @Before
    public void setup() throws IOException {
        Object arg1 = "arg1";
        Object arg2 = "arg2";
        SettableBeanProperty arg1Property = mock(SettableBeanProperty.class);
        SettableBeanProperty arg2Property = mock(SettableBeanProperty.class);
        when(arg1Property.getName()).thenReturn("name1");
        when(arg2Property.getName()).thenReturn("name2");

        if (valueInstantiator.canCreateFromObjectWith()) {
            Object[] args = new Object[]{arg1, arg2};
            when(context.findBeanProperties(null, ValueInstantiatorTest.class)).thenReturn(
                    new Object[]{arg1Property, arg2Property});
        }
    }

    @Test
    public void testGetValueClass() {
        assertEquals(Object.class, valueInstantiator.getValueClass());
    }

    @Test
    public void testGetValueTypeDesc() {
        assertEquals(VALUE_TYPE, valueInstantiator.getValueTypeDesc());
    }

    @Test
    public void testCanInstantiate() {
        assertTrue(valueInstantiator.canInstantiate());
    }

    @Test
    public void testCanCreateUsingDefault() {
        if (valueInstantiator.canCreateUsingDefault()) {
            instance = valueInstantiator.createUsingDefault(context);
            assertEquals(VALUE_TYPE, instance.getClass().getName());
        } else {
            assertFalse(valueInstantiator.canCreateUsingDefault());
        }
    }

    @Test
    public void testCanCreateFromObjectWith() {
        if (valueInstantiator.canCreateFromObjectWith()) {
            instance = valueInstantiator.createFromObjectWith(context, new Object[]{arg1, arg2});
            assertEquals(VALUE_TYPE, instance.getClass().getName());

            SettableBeanProperty[] properties = valueInstantiator.getFromObjectArguments(context);
            assertEquals(2, properties.length);
            assertEquals(arg1Property, properties[0]);
            assertEquals(arg2Property, properties[1]);
        } else {
            assertFalse(valueInstantiator.canCreateFromObjectWith());
        }
    }

    @Test
    public void testGetDefaultCreator() {
        assertNull(valueInstantiator.getDefaultCreator());
    }

    @Test
    public void testCreateUsingDefault() throws IOException {
        assertNull(valueInstantiator.getDefaultCreator());
        assertNull(valueInstantiator.createUsingDefault(context));
    }

    @Test
    public void testCreateFromObjectWith() throws IOException {
        if (valueInstantiator.canCreateFromObjectWith()) {
            AnnotatedMethod creator = mock(AnnotatedMethod.class);
            when(context.findCreator(VALUE_TYPE)).thenReturn(creator);
            instance = valueInstantiator.createFromObjectWith(context, arg1Property, arg2Property);
            assertEquals(VALUE_TYPE, instance.getClass().getName());
        } else {
            assertNull(valueInstantiator.createFromObjectWith(context, arg1Property, arg2Property));
        }
    }

    @Test
    public void testCreateFromObjectWith_missingProperty() throws IOException {
        if (valueInstantiator.canCreateFromObjectWith()) {
            SettableBeanProperty missingProperty = mock(SettableBeanProperty.class);
            when(context.findBeanProperties(null, ValueInstantiatorTest.class))
                    .thenReturn(new Object[]{arg1Property, missingProperty, arg2Property});
            try {
                valueInstantiator.createFromObjectWith(context, new Object[]{arg1, arg2});
                fail("Expected MissingPropertyException");
            } catch (MissingPropertyException e) {
                // expected
            }
        }
    }

    @Test
    public void testCreateUsingDelegate() throws IOException {
        assertNull(valueInstantiator.getDelegateType(context));
        assertNull(valueInstantiator.createUsingDelegate(context, new Object()));
    }

    @Test
    public void testCreateUsingArrayDelegate() throws IOException {
        assertNull(valueInstantiator.getArrayDelegateType(context));
        assertNull(valueInstantiator.createUsingArrayDelegate(context, new Object()));
    }

    @Test
    public void testCreateFromString() throws IOException {
        String value = "value";
        when(context.getParser().getValueAsString()).thenReturn(value);

        if (valueInstantiator.canCreateFromString()) {
            instance = valueInstantiator.createFromString(context, value);
            assertEquals(VALUE_TYPE, instance.getClass().getName());
        } else {
            assertNull(valueInstantiator.createFromString(context, value));
        }
    }

    @Test
    public void testCreateFromInt() throws IOException {
        int value = 1;
        when(context.getParser().getValueAsInt()).thenReturn(value);

        if (valueInstantiator.canCreateFromInt()) {
            instance = valueInstantiator.createFromInt(context, value);
            assertEquals(VALUE_TYPE, instance.getClass().getName());
        } else {
            assertNull(valueInstantiator.createFromInt(context, value));
        }
    }

    @Test
    public void testCreateFromLong() throws IOException {
        long value = 1L;
        when(context.getParser().getValueAsLong()).thenReturn(value);

        if (valueInstantiator.canCreateFromLong()) {
            instance = valueInstantiator.createFromLong(context, value);
            assertEquals(VALUE_TYPE, instance.getClass().getName());
        } else {
            assertNull(valueInstantiator.createFromLong(context, value));
        }
    }

    @Test
    public void testCreateFromDouble() throws IOException {
        double value = 1.0;
        when(context.getParser().getValueAsNumber()).thenReturn(new Double(value));

        if (valueInstantiator.canCreateFromDouble()) {
            instance = valueInstantiator.createFromDouble(context, value);
            assertEquals(VALUE_TYPE, instance.getClass().getName());
        } else {
            assertNull(valueInstantiator.createFromDouble(context, value));
        }
    }

    @Test
    public void testCreateFromBoolean() throws IOException {
        boolean value = true;
        when(context.getParser().getValueAsBoolean()).thenReturn(value);

        if (valueInstantiator.canCreateFromBoolean()) {
            instance = valueInstantiator.createFromBoolean(context, value);
            assertEquals(VALUE_TYPE, instance.getClass().getName());
        } else {
            assertNull(valueInstantiator.createFromBoolean(context, value));
        }
    }

}