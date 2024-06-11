import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.configuration.injection.filter.MockCandidate;
import org.mockito.internal.util.collections.Sets;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection injectionStrategy;
    private List<MockCandidate> mockCandidates;
    private Object testObject;

    @Before
    public void setup() {
        injectionStrategy = new PropertyAndSetterInjection();
        testObject = new TestClass();
        mockCandidates = Arrays.asList(new MockCandidate(TestClass.class, "field1"), new MockCandidate(TestClass.class, "field2"), new MockCandidate(OtherClass.class, "otherField"));
    }

    @Test
    public void testInjectionWithSetters() {
        Field field1 = TestClass.class.getDeclaredField("field1");
        Mockito.doReturn(new MockitoTestClass()).when(testObject).newField1();
        Mockito.doNothing().when(testObject).setField1(ArgumentMatchers.any());
        injectionStrategy.processInjection(field1, testObject, mockCandidates);
        assertSame(new MockitoTestClass(), testObject.getField1());
    }

    @Test
    public void testInjectionWithFields() {
        Mockito.doReturn(new MockitoTestClass()).when(testObject).newField1();
        injectionStrategy.processInjection(TestClass.class.getDeclaredField("field1"), testObject, mockCandidates);
        assertSame(new MockitoTestClass(), testObject.getField1());
    }

    @Test
    public void testNoMockCandidatesFound() {
        Mockito.doReturn(null).when(testObject).newField1();
        injectionStrategy.processInjection(TestClass.class.getDeclaredField("field1"), testObject, mockCandidates);
        assertNull(testObject.getField1());
    }

    @Test
    public void testMultipleMockCandidatesFound() {
        Mockito.doReturn(new MockitoTestClass()).when(testObject).newField1();
        when(mockCandidates.get(0).isMatching(ArgumentMatchers.any())).thenReturn(true);
        when(mockCandidates.get(1).isMatching(ArgumentMatchers.any())).thenReturn(true);

        injectionStrategy.processInjection(TestClass.class.getDeclaredField("field1"), testObject, mockCandidates);
        assertSame(new MockitoTestClass(), testObject.getField1());
        mockCandidates.remove(0);
        mockCandidates.remove(0);
        verify(testObject, times(2)).setField1(ArgumentMatchers.any());
    }

    @Test
    public void testInjectionWithNoArgConstructor() {
        when(testObject.getClass().getDeclaredConstructor()).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return new TestClass();
            }
        });

        injectionStrategy.processInjection(TestClass.class.getDeclaredField("field1"), testObject, mockCandidates);
        assertSame(testObject, testObject.getField1());
    }

    @Test
    public void testFailedInjection() {
        when(testObject.getClass().getDeclaredConstructor()).thenThrow(new IllegalAccessException());
        injectionStrategy.processInjection(TestClass.class.getDeclaredField("field1"), testObject, mockCandidates);
        assertNull(testObject.getField1());
    }

    @Test
    public void testInjectionWithSuperClassFields() {
        injectionStrategy.processInjection(TestClass.class.getDeclaredField("field1"), testObject, mockCandidates);
        injectionStrategy.processInjection(OtherClass.class.getDeclaredField("otherField"), testObject, mockCandidates);
        assertEquals(new TestClass(), testObject);
        assertEquals(new OtherClass(), testObject.otherField);
    }

    @Test
    public void testEmptyMockCandidates() {
        injectionStrategy.processInjection(TestClass.class.getDeclaredField("field1"), testObject, Collections.emptyList());
        assertNull(testObject.getField1());
    }

    @Test
    public void testNotInjectableFields() {
        Field finalField = TestClass.class.getDeclaredField("finalField");
        finalField.setAccessible(true);
        finalField.set(testObject, new Object());
        injectionStrategy.processInjection(finalField, testObject, mockCandidates);
        assertSame(new Object(), testObject.finalField);
    }

    @Test
    public void testStaticFields() {
        injectionStrategy.processInjection(TestClass.class.getDeclaredField("staticField"), testObject, mockCandidates);
        assertNull(testObject.staticField);
    }

    private static class TestClass {
        @InjectMocks
        private Field1 field1;
        @InjectMocks
        private OtherClass otherField;
        @InjectMocks
        private static Field1 staticField;
        private Field1 field2;
        private static Field1 finalField;

        public Field1 getField1() {
            return field1;
        }

        public void setField1(Field1 field1) {
            this.field1 = field1;
        }

        public OtherClass getOtherField() {
            return otherField;
        }

        public static Field1 getStaticField() {
            return staticField;
        }

        public TestClass() {
            field1 = new TestClassField1();
            otherField = new OtherClass();
            finalField = new TestClassFinalField();
        }

        public Field1 newField1() {
            return new TestClassField1();
        }
    }

    private static class TestClassField1 {}
    private static class OtherClass {
        private Field otherField;

        @InjectMocks
        public void setOtherField(Field otherField) {
            this.otherField = otherField;
        }
    }
    private static class TestClassFinalField {}
}