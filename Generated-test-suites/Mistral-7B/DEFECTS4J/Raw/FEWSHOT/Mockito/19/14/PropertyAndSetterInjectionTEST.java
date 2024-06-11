package org.mockito.internal.configuration.injection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.util.reflection.Reflections.getDeclaredFields;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.collections.Sets;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.internal.util.reflection.Reflections;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection injectionStrategy;
    private MockCandidateFilter mockCandidateFilter;
    private ListUtil listUtil;
    private SuperTypesLastSorter sorter;

    @Before
    public void setUp() {
        mockCandidateFilter = new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter()));
        injectionStrategy = new PropertyAndSetterInjection();
        listUtil = new ListUtil();
        sorter = new SuperTypesLastSorter();
    }

    @Test
    public void testInitializeInjectMocksField() throws Exception {
        Object mock = Reflections.newInstance(MockitoTestClass.class);
        Field field = Reflections.getDeclaredField(MockitoTestClass.class, "injectMocksField");
        FieldInitializationReport report = injectionStrategy.initializeInjectMocksField(field, mock);
        assertTrue(report.isInitialized());
    }

    @Test(expected = MockitoException.class)
    public void testInitializeInjectMocksFieldThrowsException() throws Exception {
        Object mock = Reflections.newInstance(MockitoTestClass.class);
        Field field = Reflections.getDeclaredField(MockitoTestClass.class, "injectMocksFieldThrowsException");
        injectionStrategy.initializeInjectMocksField(field, mock);
    }

    @Test
    public void testProcessInjection() throws Exception {
        Object mock = Reflections.newInstance(MockitoTestClass.class);
        Field field = Reflections.getDeclaredField(MockitoTestClass.class, "injectMocksField");
        Set<Object> mockCandidates = Sets.newHashSet();
        mockCandidates.add(Reflections.newInstance(MockitoTestClass.MockedType.class));
        assertTrue(injectionStrategy.processInjection(field, mock, mockCandidates));
    }

    @Test
    public void testProcessInjectionNoMockFound() throws Exception {
        Object mock = Reflections.newInstance(MockitoTestClass.class);
        Field field = Reflections.getDeclaredField(MockitoTestClass.class, "injectMocksField");
        Set<Object> mockCandidates = Sets.newHashSet();
        assertFalse(injectionStrategy.processInjection(field, mock, mockCandidates));
    }

    @Test
    public void testInjectMockCandidates() throws Exception {
        Object mock = Reflections.newInstance(MockitoTestClass.class);
        Class<?> awaitingInjectionClazz = MockitoTestClass.class;
        Set<Object> mockCandidates = Sets.newHashSet();
        mockCandidates.add(Reflections.newInstance(MockitoTestClass.MockedType.class));
        assertTrue(injectionStrategy.injectMockCandidates(awaitingInjectionClazz, mockCandidates, mock));
    }

    @Test
    public void testInjectMockCandidatesNoMockFound() throws Exception {
        Object mock = Reflections.newInstance(MockitoTestClass.class);
        Class<?> awaitingInjectionClazz = MockitoTestClass.class;
        Set<Object> mockCandidates = Sets.newHashSet();
        assertFalse(injectionStrategy.injectMockCandidates(awaitingInjectionClazz, mockCandidates, mock));
    }

    @Test
    public void testInjectMockCandidatesOnFields() throws Exception {
        Object mock = Reflections.newInstance(MockitoTestClass.class);
        Set<Object> mockCandidates = Sets.newHashSet();
        mockCandidates.add(Reflections.newInstance(MockitoTestClass.MockedType.class));
        List<Field> orderedInstanceFields = getDeclaredFields(MockitoTestClass.class);

        assertTrue(injectionStrategy.injectMockCandidatesOnFields(mockCandidates, mock, false, orderedInstanceFields));
        assertTrue(injectionStrategy.injectMockCandidatesOnFields(mockCandidates, mock, false, orderedInstanceFields));
    }

    @Test
    public void testInjectMockCandidatesOnFieldsNoMockFound() throws Exception {
        Object mock = Reflections.newInstance(MockitoTestClass.class);
        Set<Object> mockCandidates = Sets.newHashSet();
        List<Field> orderedInstanceFields = getDeclaredFields(MockitoTestClass.class);

        assertFalse(injectionStrategy.injectMockCandidatesOnFields(mockCandidates, mock, false, orderedInstanceFields));
        assertFalse(injectionStrategy.injectMockCandidatesOnFields(mockCandidates, mock, false, orderedInstanceFields));
    }

    @Test
    public void testOrderedInstanceFieldsFrom() throws Exception {
        Class<?> awaitingInjectionClazz = MockitoTestClass.class;
        List<Field> expectedOrderedInstanceFields = Arrays.asList(Reflections.getDeclaredField(MockitoTestClass.class, "field1"),
                Reflections.getDeclaredField(MockitoTestClass.class, "field2"), Reflections.getDeclaredField(MockitoTestClass.class, "field3"));

        List<Field> orderedInstanceFields = injectionStrategy.orderedInstanceFieldsFrom(awaitingInjectionClazz);
        assertTrue(ListUtil.equals(orderedInstanceFields, expectedOrderedInstanceFields));
    }
}

class MockitoTestClass {

    @InjectMocks
    private static class InnerClass {
        private String field1;
        private String field2;
        private String field3;

        public String getField1() {
            return field1;
        }

        @Inject
        public void setField1(String field1) {
            this.field1 = field1;
        }

        public String getField2() {
            return field2;
        }

        @Inject
        public void setField2(String field2) {
            this.field2 = field2;
        }

        public String getField3() {
            return field3;
        }

        @Inject
        public void setField3(String field3) {
            this.field3 = field3;
        }
    }

    @InjectMocks
    private static class AnotherInnerClass {
        private String field1;
        private String field2;

        public String getField1() {
            return field1;
        }

        @Inject
        public void setField1(String field1) {
            this.field1 = field1;
        }

        public String getField2() {
            return field2;
        }

        @Inject
        public void setField2(String field2) {
            this.field2 = field2;
        }

        private String field3;

        public String getField3() {
            return field3;
        }
    }

    @InjectMocks
    private static class MockitoTestClass {
        @InjectMocks
        private InnerClass innerClass;
        @InjectMocks
        private AnotherInnerClass anotherInnerClass;
        @InjectMocks
        private static InnerClass staticInnerClass;

        @Inject
        private MockCandidateFilter mockCandidateFilter;

        @Inject
        private FieldInitializer fieldInitializer;

        @Inject
        private SuperTypesLastSorter superTypesLastSorter;

        @Inject
        private ListUtil listUtil;

        @Inject
        private Set<Object> mocks;

        @Inject
        private Set<Object> mocksToBeInjected;

        private String injectMocksField;
        private String injectMocksFieldThrowsException;

        public String getInjectMocksField() {
            return injectMocksField;
        }

        @Inject
        public void setInjectMocksField(String injectMocksField) {
            this.injectMocksField = injectMocksField;
        }

        public String getInjectMocksFieldThrowsException() {
            throw new RuntimeException();
        }
    }

    static class MockedType {}
}