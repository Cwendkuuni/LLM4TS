import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidateWithSingleMockAndField() {
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field field = new Field("testField", String.class);
        Object fieldInstance = spy(new Object() {
            private String testField;

            @Override
            public String getTestField() {
                return testField;
            }

            public void setTestField(String testField) {
                this.testField = testField;
            }
        });

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        result.thenInject();

        Assert.assertEquals("Test field value should be set", "expectedValue", fieldInstance.getTestField());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidateWithSingleMockAndFieldFailure() {
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field field = new Field("testField", String.class);
        Object fieldInstance = spy(new Object() {
            private String testField;

            @Override
            public String getTestField() {
                return testField;
            }

            public void setTestField(String testField) {
                this.testField = testField;
            }
        });

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        try {
            result.thenInject();
            Assert.fail("Exception should be thrown");
        } catch (MockitoException e) {
            // expected
        }

        String expectedMessage = "Problems injecting dependency in testField";
        String actualMessage = e.getMessage();
        Assert.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testFilterCandidateWithMultipleMocksAndField() {
        Collection<Object> mocks = Collections.singletonList(mock(Object1.class));
        mocks.add(mock(Object2.class));
        Field field = new Field("testField", String.class);
        Object fieldInstance = spy(new Object() {
            private String testField;

            @Override
            public String getTestField() {
                return testField;
            }

            public void setTestField(String testField) {
                this.testField = testField;
            }
        });

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        result.thenInject();

        Assert.assertFalse("Injector should return false", result.canInject());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidateWithMultipleMocksAndFieldFailure() {
        Collection<Object> mocks = Collections.singletonList(mock(Object1.class));
        mocks.add(mock(Object2.class));
        Field field = new Field("testField", String.class);
        Object fieldInstance = spy(new Object() {
            private String testField;

            @Override
            public String getTestField() {
                return testField;
            }

            public void setTestField(String testField) {
                this.testField = testField;
            }
        });

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        try {
            result.thenInject();
            Assert.fail("Exception should be thrown");
        } catch (MockitoException e) {
            // expected
        }

        String expectedMessage = "Problems injecting dependency in testField";
        String actualMessage = e.getMessage();
        Assert.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testFilterCandidateWithSingleMockAndFieldAccess() {
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field field = new Field("testField", String.class);
        Object fieldInstance = mock(Object.class);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        result.thenInject();

        // This will fail as there is no setter for fieldAccessMock
        verify(fieldInstance).setTestField("expectedValue");
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidateWithSingleMockAndFieldAccessFailure() {
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field field = new Field("testField", String.class);
        Object fieldInstance = mock(Object.class);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        try {
            result.thenInject();
            Assert.fail("Exception should be thrown");
        } catch (MockitoException e) {
            // expected
        }

        String expectedMessage = "Problems injecting dependency in testField";
        String actualMessage = e.getMessage();
        Assert.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testFilterCandidateWithMultipleMocksAndFieldAccess() {
        Collection<Object> mocks = Collections.singletonList(mock(Object1.class));
        mocks.add(mock(Object2.class));
        Field field = new Field("testField", String.class);
        Object fieldInstance = mock(Object.class);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        result.thenInject();

        // This will fail as there is no setter for fieldAccessMock
        verify(fieldInstance, never()).setTestField(anyString());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidateWithMultipleMocksAndFieldAccessFailure() {
        Collection<Object> mocks = Collections.singletonList(mock(Object1.class));
        mocks.add(mock(Object2.class));
        Field field = new Field("testField", String.class);
        Object fieldInstance = mock(Object.class);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        try {
            result.thenInject();
            Assert.fail("Exception should be thrown");
        } catch (MockitoException e) {
            // expected
        }

        String expectedMessage = "Problems injecting dependency in testField";
        String actualMessage = e.getMessage();
        Assert.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testFilterCandidateWithSingleMockAndNullField() {
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field field = null;
        Object fieldInstance = mock(Object.class);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // NullFieldException should be thrown
        try {
            result.thenInject();
            Assert.fail("Exception should be thrown");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithSingleMockAndNullFieldInstance() {
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field field = new Field("testField", String.class);
        Object fieldInstance = null;

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        try {
            result.thenInject();
            Assert.fail("Exception should be thrown");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidateWithEmptyMockCollection() {
        Collection<Object> mocks = Collections.emptyList();
        Field field = new Field("testField", String.class);
        Object fieldInstance = mock(Object.class);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        try {
            result.thenInject();
            Assert.fail("Exception should be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFilterCandidateWithSingleMockAndValidInput() {
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field field = new Field("testField", String.class);
        Object fieldInstance = mock(Object.class);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        result.thenInject();

        // No exception should be thrown
    }

    private static class Object1 {}

    private static class Object2 {}
}