package org.mockito.internal.configuration.injection;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.mockitoAppState;
import org.mockito.internal.configuration.mockitoSession;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.FieldUtils;
import org.mockito.mock.MockitoCore;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerImpl;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FinalMockCandidateFilterTest {

    private static final String FIELD_NAME = "testField";
    private static final String FIELD_TYPE = "java.lang.String";
    private static final String MOCKITO_APP_STATE_NAME = "mockitoAppState";
    private static final String MOCKITO_SESSION_NAME = "mockitoSession";

    @Test
    public void testFilterCandidate_OneMock_OneField_FieldSetterSuccess() {
        // Given
        MockMaker mockMaker = new MockMakerImpl();
        mockitoAppState mockitoAppState = new mockitoAppState();
        mockitoSession mockitoSession = new mockitoSession();

        mockitoAppState.setMockitoSession(mockitoSession);
        PowerMockito.mockStatic(org.mockito.Mockito.class);
        when(org.mockito.Mockito.mock(FIELD_TYPE)).thenReturn(mock(FIELD_TYPE));

        List<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);

        Field field = FieldUtils.getField(Object.class, FIELD_NAME, false);
        Object fieldInstance = "testValue";

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        Assert.assertNotNull(injecter);
        Assert.assertTrue(injecter.thenInject());

        Object injectedValue = Whitebox.getInternalState(mock, FIELD_NAME);
        Assert.assertEquals(fieldInstance, injectedValue);

        verify(mockitoAppState).setMockitoSession(mockitoSession);
        verify(mockitoSession).addMock(mock);
        verify(mockitoSession).processAnnotatedFields(mock, Collections.singletonList(field));
    }

    @Test
    public void testFilterCandidate_OneMock_OneField_FieldSetterFailure() {
        // Given
        MockMaker mockMaker = new MockMakerImpl();
        mockitoAppState mockitoAppState = new mockitoAppState();
        mockitoSession mockitoSession = new mockitoSession();

        mockitoAppState.setMockitoSession(mockitoSession);
        PowerMockito.mockStatic(org.mockito.Mockito.class);
        when(org.mockito.Mockito.mock(FIELD_TYPE)).thenThrow(new RuntimeException());

        List<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);

        Field field = FieldUtils.getField(Object.class, FIELD_NAME, false);
        Object fieldInstance = "testValue";

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When & Then
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertNotNull(injecter);
        Assert.assertFalse(injecter.thenInject());

        verify(mockitoAppState).setMockitoSession(mockitoSession);
        verify(mockitoSession, Mockito.never()).addMock(mock);
        verify(mockitoSession, Mockito.never()).processAnnotatedFields(mock, Collections.singletonList(field));
    }

    @Test
    public void testFilterCandidate_OneMock_OneField_NoFieldSetter() {
        // Given
        MockMaker mockMaker = new MockMakerImpl();
        mockitoAppState mockitoAppState = new mockitoAppState();
        mockitoSession mockitoSession = new mockitoSession();

        mockitoAppState.setMockitoSession(mockitoSession);
        PowerMockito.mockStatic(org.mockito.Mockito.class);
        when(org.mockito.Mockito.mock(FIELD_TYPE)).thenReturn(mock(FIELD_TYPE));

        List<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);

        Field field = FieldUtils.getField(Object.class, FIELD_NAME, false);
        Object fieldInstance = "testValue";

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        Assert.assertNotNull(injecter);
        Assert.assertFalse(injecter.thenInject());

        verify(mockitoAppState).setMockitoSession(mockitoSession);
        verify(mockitoSession, Mockito.never()).addMock(mock);
        verify(mockitoSession, Mockito.never()).processAnnotatedFields(mock, Collections.singletonList(field));
    }

    @Test
    public void testFilterCandidate_MultipleMocks_OneField_FirstMockMatches() {
        // Given
        MockMaker mockMaker = new MockMakerImpl();
        mockitoAppState mockitoAppState = new mockitoAppState();
        mockitoSession mockitoSession = new mockitoSession();

        mockitoAppState.setMockitoSession(mockitoSession);
        PowerMockito.mockStatic(org.mockito.Mockito.class);
        when(org.mockito.Mockito.mock(FIELD_TYPE)).thenReturn(mock(FIELD_TYPE));

        List<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = FieldUtils.getField(Object.class, FIELD_NAME, false);
        Object fieldInstance = "testValue";

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        Assert.assertNotNull(injecter);
        Assert.assertTrue(injecter.thenInject());

        Object injectedValue = Whitebox.getInternalState(mock1, FIELD_NAME);
        Assert.assertEquals(fieldInstance, injectedValue);

        verify(mockitoAppState).setMockitoSession(mockitoSession);
        verify(mockitoSession).addMock(mock1);
        verify(mockitoSession).processAnnotatedFields(mock1, Collections.singletonList(field));
        verify(mockitoSession, Mockito.never()).addMock(mock2);
        verify(mockitoSession, Mockito.never()).processAnnotatedFields(mock2, Collections.singletonList(field));
    }

    @Test
    public void testFilterCandidate_MultipleMocks_OneField_NoMatch() {
        // Given
        MockMaker mockMaker = new MockMakerImpl();
        mockitoAppState mockitoAppState = new mockitoAppState();
        mockitoSession mockitoSession = new mockitoSession();

        mockitoAppState.setMockitoSession(mockitoSession);
        PowerMockito.mockStatic(org.mockito.Mockito.class);
        when(org.mockito.Mockito.mock(FIELD_TYPE)).thenReturn(mock(FIELD_TYPE));

        List<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = FieldUtils.getField(Object.class, FIELD_NAME, false);
        Object fieldInstance = "testValue";

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        Assert.assertNotNull(injecter);
        Assert.assertFalse(injecter.thenInject());

        verify(mockitoAppState).setMockitoSession(mockitoSession);
        verify(mockitoSession, Mockito.never()).addMock(mock1);
        verify(mockitoSession, Mockito.never()).processAnnotatedFields(mock1, Collections.singletonList(field));
        verify(mockitoSession, Mockito.never()).addMock(mock2);
        verify(mockitoSession, Mockito.never()).processAnnotatedFields(mock2, Collections.singletonList(field));
    }

    @Test
    public void testFilterCandidate_MultipleMocks_MultipleFields_MultipleMatches() {
        // Given
        MockMaker mockMaker = new MockMakerImpl();
        mockitoAppState mockitoAppState = new mockitoAppState();
        mockitoSession mockitoSession = new mockitoSession();

        mockitoAppState.setMockitoSession(mockitoSession);
        PowerMockito.mockStatic(org.mockito.Mockito.class);
        when(org.mockito.Mockito.mock(String.class)).thenReturn(mock(String.class));

        List<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Field field1 = FieldUtils.getField(Object.class, "field1", false);
        Object field1Instance1 = "field1Value1";
        Field field2 = FieldUtils.getField(Object.class, "field2", false);
        Object field2Instance1 = "field2Value1";

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        OngoingInjecter injecter1 = filter.filterCandidate(mocks, field1, field1Instance1);
        OngoingInjecter injecter2 = filter.filterCandidate(mocks, field2, field2Instance1);

        // Then
        Assert.assertNotNull(injecter1);
        Assert.assertTrue(injecter1.thenInject());

        Object injectedValue1 = Whitebox.getInternalState(mock1, "field1");
        Assert.assertEquals(field1Instance1, injectedValue1);

        Assert.assertNotNull(injecter2);
        Assert.assertTrue(injecter2.thenInject());

        Object injectedValue2 = Whitebox.getInternalState(mock2, "field2");
        Assert.assertEquals(field2Instance1, injectedValue2);

        verify(mockitoAppState).setMockitoSession(mockitoSession);
        verify(mockitoSession).addMock(mock1);
        verify(mockitoSession).processAnnotatedFields(mock1, Arrays.asList(field1, field2));
        verify(mockitoSession).addMock(mock2);
        verify(mockitoSession).processAnnotatedFields(mock2, Arrays.asList(field1, field2));
    }

    @Test
    public void testFilterCandidate_MultipleMocks_MultipleFields_NoMatches() {
        // Given
        MockMaker mockMaker = new MockMakerImpl();
        mockitoAppState mockitoAppState = new mockitoAppState();
        mockitoSession mockitoSession = new mockitoSession();

        mockitoAppState.setMockitoSession(mockitoSession);
        PowerMockito.mockStatic(org.mockito.Mockito.class);
        when(org.mockito.Mockito.mock(String.class)).thenReturn(mock(String.class));

        List<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Field field1 = FieldUtils.getField(Object.class, "field1", false);
        Object field1Instance1 = "field1Value1";
        Field field2 = FieldUtils.getField(Object.class, "field2", false);
        Object field2Instance1 = "field2Value1";

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        OngoingInjecter injecter1 = filter.filterCandidate(mocks, field1, field1Instance1);
        OngoingInjecter injecter2 = filter.filterCandidate(mocks, field2, field2Instance1);

        // Then
        Assert.assertNotNull(injecter1);
        Assert.assertFalse(injecter1.thenInject());

        Assert.assertNotNull(injecter2);
        Assert.assertFalse(injecter2.thenInject());

        verify(mockitoAppState).setMockitoSession(mockitoSession);
        verify(mockitoSession, Mockito.never()).addMock(mock1);
        verify(mockitoSession, Mockito.never()).processAnnotatedFields(mock1, Collections.singletonList(field1));
        verify(mockitoSession, Mockito.never()).processAnnotatedFields(mock1, Collections.singletonList(field2));
        verify(mockitoSession, Mockito.never()).addMock(mock2);
        verify(mockitoSession, Mockito.never()).processAnnotatedFields(mock2, Collections.singletonList(field1));
        verify(mockitoSession, Mockito.never()).processAnnotatedFields(mock2, Collections.singletonList(field2));
    }

    @Test
    public void testFilterCandidate_NullCollection() {
        // Given
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Field field = FieldUtils.getField(Object.class, FIELD_NAME, false);
        Object fieldInstance = "testValue";

        // When
        OngoingInjecter injecter = filter.filterCandidate(null, field, fieldInstance);

        // Then
        Assert.assertNotNull(injecter);
        Assert.assertFalse(injecter.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_NullField() {
        // Given
        List<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        filter.filterCandidate(mocks, null, "testValue");

        // Then
        // An exception should be thrown
    }

    @Test
    public void testFilterCandidate_NullFieldInstance() {
        // Given
        List<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);

        Field field = FieldUtils.getField(Object.class, FIELD_NAME, false);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, null);

        // Then
        Assert.assertNotNull(injecter);
        Assert.assertFalse(injecter.thenInject());
    }
}