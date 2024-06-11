package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.stubs.MockitoStubs;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertNotNull;
import static org.mockito.internal.util.reflection.Reflections.getFieldValue;
import static org.mockito.internal.util.reflection.Reflections.setFieldValue;

public class FinalMockCandidateFilterTest {
    private Collection<Object> mocks;
    private Object fieldInstance;
    private Field field;

    @Before
    public void setup() {
        mocks = new ArrayList<>();
        mocks.add(MockitoStubs.spy(new Foo()));
        fieldInstance = new Bar();
        field = Reflections.getField(Bar.class, "field");
    }

    @Test
    public void testFilterCandidateWithOneMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
    }

    @Test
    public void testInjectWithOneMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        assertNotNull(result);
        assertEquals(mocks.get(0), result);
    }

    @Test
    public void testInjectWithMultipleMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();

        assertNull(getFieldValue(fieldInstance, "field"));
    }

    @Test(expected = RuntimeException.class)
    public void testFailedInjectWithOneMock() {
        when(mocks.get(0).getClass().getDeclaredField("notExistingField"))
                .thenThrow(new RuntimeException("Test exception"));

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        filter.filterCandidate(mocks, new Field("notExistingField", String.class, Bar.class), fieldInstance);
    }

    @Test
    public void testInjectWithPropertySetter() {
        Foo mock = new Foo();
        when(mock.getClass().getDeclaredField("property"))
                .thenReturn(new Field("property", String.class, Foo.class));
        when(mock.getClass().getDeclaredField("property").getWriteMethod())
                .thenReturn(new BeanPropertySetter(mock, new Field("property", String.class, Foo.class)));

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        assertNotNull(result);
        assertEquals(mocks.get(0), result);
        assertEquals("test", getFieldValue(mocks.get(0), "property"));

        setFieldValue(fieldInstance, "field", "testResult");
        assertEquals("testResult", getFieldValue(fieldInstance, "field"));
    }

    @Test
    public void testInjectWithFieldSetter() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();

        assertEquals("testField", getFieldValue(fieldInstance, "field"));
    }

    @Test
    public void testInjectWithNullMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(null, field, fieldInstance);

        assertNotNull(injecter);
    }

    @Test
    public void testInjectWithNullField() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, null, fieldInstance);

        assertNotNull(injecter);
    }

    @Test
    public void testInjectWithNullFieldInstance() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, null);

        assertNotNull(injecter);
    }
}

class Bar {
    private String field;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}

class Foo {
    private String property;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }
}