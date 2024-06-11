package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injectingbyname.NamedMock;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.FieldType;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stub.Answer;
import org.mockito.stub.Answer2;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class FinalMockCandidateFilterTest {

    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;
    private NamedMock namedMock;
    private Object actualMock;

    @Before
    public void setUp() {
        mocks = Arrays.asList(mock(NamedMock.class), mock(NamedMock.class), mock(NamedMock.class));
        field = FieldType.findField(NamedMock.class, "fieldToMock");
        fieldInstance = spy(mock(NamedMock.class));
        namedMock = (NamedMock) mocks.iterator().next();
    }

    @Test
    public void testFilterCandidateWithOneMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertSame(namedMock, injecter.getMock());
    }

    @Test
    public void testInjectWithSingleFieldAndOneMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Answer<Object> answer = mock(Answer.class);
        FieldSetter setter = new FieldSetter(fieldInstance, field);
        injecter.inject(answer);

        verify(namedMock).answer(answer);
    }

    @Test(expected = MockitoException.class)
    public void testInjectWithSingleFieldAndOneMockThrowsException() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        try {
            injecter.inject(mock(Answer2.class));
        } catch (Exception e) {
            throw new MockitoException("Unexpected exception", e);
        }
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(injecter.thenInject());
    }

    @Test
    public void testFilterCandidateWithNull() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(null, field, fieldInstance);

        assertNull(injecter);
    }

    @Test
    public void testFilterCandidateWithNullField() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, null, fieldInstance);

        assertNull(injecter);
    }

    @Test
    public void testFilterCandidateWithNullFieldInstance() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, null);

        assertNull(injecter);
    }
}