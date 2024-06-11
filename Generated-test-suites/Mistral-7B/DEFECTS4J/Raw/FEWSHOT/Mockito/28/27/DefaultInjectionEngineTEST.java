package org.mockito.internal.configuration;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.Fields;
import org.mockito.internal.util.reflection.FieldType;
import org.mockito.mockitosis.annotation.InjectMocks;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class DefaultInjectionEngineTest {

    @Test
    public void testInjectMocksOnFields() {
        Reporter reporter = mock(Reporter.class);
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();

        Class<Foo> fooClass = Foo.class;
        Class<Bar> barClass = Bar.class;
        Class<Baz> bazClass = Baz.class;

        Foo foo = mock(Foo.class);
        Bar bar = mock(Bar.class);
        Baz baz = mock(Baz.class);

        Set<Object> mocks = new HashSet<>(Arrays.asList(foo, bar, baz));

        Object testClassInstance = spy(new TestClass());

        injectionEngine.injectMocksOnFields(getFieldsAnnotatedWithInjectMocks(fooClass), mocks, testClassInstance);

        Field field1 = getFieldAnnotatedWithInjectMocks(fooClass, "field1");
        Field field2 = getFieldAnnotatedWithInjectMocks(fooClass, "field2");

        Assert.assertNotNull(field1.get(testClassInstance));
        Assert.assertNotNull(field2.get(testClassInstance));

        Assert.assertFalse(mocks.contains(field1.get(testClassInstance)));
        Assert.assertFalse(mocks.contains(field2.get(testClassInstance)));

        verifyNoMoreInteractions(foo);
        verifyNoMoreInteractions(bar);
        verifyNoMoreInteractions(baz);

        injectionEngine.injectMocksOnFields(getFieldsAnnotatedWithInjectMocks(barClass), mocks, testClassInstance);

        Field field3 = getFieldAnnotatedWithInjectMocks(barClass, "field3");

        Assert.assertNotNull(field3.get(testClassInstance));

        Assert.assertFalse(mocks.contains(field3.get(testClassInstance)));

        verifyNoMoreInteractions(foo);
        verifyNoMoreInteractions(bar);
        verifyNoMoreInteractions(baz);

        injectionEngine.injectMocksOnFields(getFieldsAnnotatedWithInjectMocks(bazClass), mocks, testClassInstance);

        Field field4 = getFieldAnnotatedWithInjectMocks(bazClass, "field4");

        Assert.assertNull(field4.get(testClassInstance));

        verifyNoMoreInteractions(foo);
        verifyNoMoreInteractions(bar);
        verifyNoMoreInteractions(baz);
    }

    private Set<Field> getFieldsAnnotatedWithInjectMocks(Class<?> clazz) {
        return getFieldsAnnotatedWithAnnotation(clazz, InjectMocks.class);
    }

    private Field getFieldAnnotatedWithInjectMocks(Class<?> clazz, String fieldName) {
        Field field = null;
        for (Field f : getFields(clazz)) {
            if (f.getName().equals(fieldName)) {
                if (f.isAnnotationPresent(InjectMocks.class)) {
                    field = f;
                    break;
                }
            }
        }
        return field;
    }

    private Set<Field> getFields(Class<?> clazz) {
        return Fields.getDeclaredFields(clazz, getFieldTypes());
    }

    private Class<? extends FieldType>[] getFieldTypes() {
        return new Class[]{FieldType.CLASS, FieldType.ANNOTATED};
    }

}

class TestClass {
    @InjectMocks
    private Foo field1;

    @InjectMocks
    private Bar field2;

    @InjectMocks
    private Baz field3;

    @InjectMocks
    private Qux field4;
}

interface Foo {}

interface Bar extends Foo {}

interface Baz extends Bar {}

interface Qux extends Baz {}