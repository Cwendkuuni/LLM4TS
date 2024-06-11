import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.JUnit4;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.Whitebox;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(JUnit4.class)
public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMock_propertySetSuccess() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Object mock = Mockito.spy(new Object());
        Object fieldInstance = new Object();
        Field field = Mockito.spy(new Field("testField") {
            @Override
            public Class<?> getType() {
                return Object.class;
            }
        });

        List<Object> mocks = new ArrayList<>();
        mocks.add(mock);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Whitebox.invokeMethod(injecter, "thenInject");

        BeanPropertySetter propertySetter = new BeanPropertySetter(fieldInstance, field);
        propertySetter.setValue(mock);

        // assert that the property was set
        Object actualValue = Whitebox.getInternalState(fieldInstance, "testField");
        Assert.assertSame(mock, actualValue);

        // assert that the injecter returned the mock
        Object result = Whitebox.getInternalState(injecter, "instance");
        Assert.assertSame(mock, result);
    }

    @Test
    public void testFilterCandidate_singleMock_propertySetFailure() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Object mock = Mockito.spy(new Object());
        Object fieldInstance = new Object();
        Field field = Mockito.spy(new Field("testField") {
            @Override
            public Class<?> getType() {
                return Object.class;
            }
        });

        List<Object> mocks = new ArrayList<>();
        mocks.add(mock);

        try {
            filter.filterCandidate(mocks, field, fieldInstance);
            Assert.fail("Should have thrown an exception");
        } catch (RuntimeException e) {
            // expected
        }

        try {
            Whitebox.invokeMethod(filter, "filterCandidate", mocks, field, fieldInstance);
            Assert.fail("Should have thrown an exception");
        } catch (RuntimeException e) {
            // expected
        }

        BeanPropertySetter propertySetter = new BeanPropertySetter(fieldInstance, field);
        try {
            propertySetter.set(mock);
            Assert.fail("Should have thrown an exception");
        } catch (RuntimeException e) {
            // expected
        }

        // assert that nothing was set
        Object actualValue = Whitebox.getInternalState(fieldInstance, "testField");
        Assert.assertNull(actualValue);
    }

    @Test
    public void testFilterCandidate_singleMock_fieldSetSuccess() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Object mock = Mockito.spy(new Object());
        Object fieldInstance = new Object();
        Field field = Mockito.spy(new Field("testField") {
            @Override
            public Class<?> getType() {
                return Object.class;
            }
        });

        List<Object> mocks = new ArrayList<>();
        mocks.add(mock);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Whitebox.invokeMethod(injecter, "thenInject");

        FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
        fieldSetter.setValue(mock);

        // assert that the field was set
        Object actualValue = Whitebox.getInternalState(fieldInstance, "testField");
        Assert.assertSame(mock, actualValue);

        // assert that the injecter returned the mock
        Object result = Whitebox.getInternalState(injecter, "instance");
        Assert.assertSame(mock, result);
    }

    @Test
    public void testFilterCandidate_singleMock_fieldSetFailure() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Object mock = Mockito.spy(new Object());
        Object fieldInstance = new Object();
        Field field = Mockito.spy(new Field("testField") {
            @Override
            public Class<?> getType() {
                return Object.class;
            }
        });

        List<Object> mocks = new ArrayList<>();
        mocks.add(mock);

        try {
            filter.filterCandidate(mocks, field, fieldInstance);
            Assert.fail("Should have thrown an exception");
        } catch (RuntimeException e) {
            // expected
        }

        try {
            Whitebox.invokeMethod(filter, "filterCandidate", mocks, field, fieldInstance);
            Assert.fail("Should have thrown an exception");
        } catch (RuntimeException e) {
            // expected
        }

        FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
        try {
            fieldSetter.set(mock);
            Assert.fail("Should have thrown an exception");
        } catch (RuntimeException e) {
            // expected
        }

        // assert that nothing was set
        Object actualValue = Whitebox.getInternalState(fieldInstance, "testField");
        Assert.assertNull(actualValue);
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Object mock1 = Mockito.spy(new Object());
        Object mock2 = Mockito.spy(new Object());
        Object fieldInstance = new Object();
        Field field = Mockito.spy(new Field("testField") {
            @Override
            public Class<?> getType() {
                return Object.class;
            }
        });

        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Whitebox.invokeMethod(injecter, "thenInject");

        // assert that no mock was set
        Object actualValue = Whitebox.getInternalState(fieldInstance, "testField");
        Assert.assertNull(actualValue);

        // assert that the injecter returned null
        Object result = Whitebox.getInternalState(injecter, "instance");
        Assert.assertNull(result);
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_collectionNull() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Object fieldInstance = new Object();
        Field field = Mockito.spy(new Field("testField") {
            @Override
            public Class<?> getType() {
                return Object.class;
            }
        });

        try {
            filter.filterCandidate(null, field, fieldInstance);
            Assert.fail("Should have thrown an exception");
        } catch (NullPointerException e) {
            // expected, but MockitoException is expected by the caller
            throw new MockitoException("Mocks collection can't be null", e);
        }

        try {
            Whitebox.invokeMethod(filter, "filterCandidate", null, field, fieldInstance);
            Assert.fail("Should have thrown an exception");
        } catch (NullPointerException e) {
            // expected, but MockitoException is expected by the caller
            throw new MockitoException("Mocks collection can't be null", e);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_fieldNull() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Object mock = Mockito.spy(new Object());
        Object fieldInstance = new Object();
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock);

        try {
            filter.filterCandidate(mocks, null, fieldInstance);
            Assert.fail("Should have thrown an exception");
        } catch (NullPointerException e) {
            // expected
        }

        try {
            Whitebox.invokeMethod(filter, "filterCandidate", mocks, null, fieldInstance);
            Assert.fail("Should have thrown an exception");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_fieldInstanceNull() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Object mock = Mockito.spy(new Object());
        Field field = Mockito.spy(new Field("testField") {
            @Override
            public Class<?> getType() {
                return Object.class;
            }
        });
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock);

        try {
            filter.filterCandidate(mocks, field, null);
            Assert.fail("Should have thrown an exception");
        } catch (NullPointerException e) {
            // expected
        }

        try {
            Whitebox.invokeMethod(filter, "filterCandidate", mocks, field, null);
            Assert.fail("Should have thrown an exception");
        } catch (NullPointerException e) {
            // expected
        }
    }
}