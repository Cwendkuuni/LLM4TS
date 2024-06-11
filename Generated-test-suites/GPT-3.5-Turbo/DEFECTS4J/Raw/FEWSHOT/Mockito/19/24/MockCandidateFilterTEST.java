package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

public class MockCandidateFilterTest {

    private MockCandidateFilter filter;
    private Field field;
    private Object instance;
    private Collection<Object> mocks;

    @Before
    public void setup() {
        filter = new MockCandidateFilterImpl();
        field = mock(Field.class);
        instance = new Object();
        mocks = null;
    }

    @Test
    public void testFilterCandidate() {
        OngoingInjecter result = filter.filterCandidate(mocks, field, instance);

        assertNotNull(result);
        assertEquals(field, result.getField());
        assertEquals(instance, result.getFieldInstance());
    }
}

class MockCandidateFilterImpl implements MockCandidateFilter {

    public OngoingInjecter filterCandidate(
            Collection<Object> mocks,
            Field fieldToBeInjected,
            Object fieldInstance
    ) {
        return new OngoingInjecterImpl(fieldToBeInjected, fieldInstance);
    }
}

class OngoingInjecterImpl implements OngoingInjecter {

    private Field field;
    private Object fieldInstance;

    OngoingInjecterImpl(Field field, Object fieldInstance) {
        this.field = field;
        this.fieldInstance = fieldInstance;
    }

    public Field getField() {
        return field;
    }

    public Object getFieldInstance() {
        return fieldInstance;
    }
}

interface OngoingInjecter {

    Field getField();

    Object getFieldInstance();

}