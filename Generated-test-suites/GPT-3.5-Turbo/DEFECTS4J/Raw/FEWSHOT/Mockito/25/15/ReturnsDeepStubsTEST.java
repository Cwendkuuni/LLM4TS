import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;


public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

    @Test
    public void testAnswer() throws Throwable {
        // Create mock container
        InvocationContainerImpl container = new InvocationContainerImpl();

        // Create stubbed invocation matchers
        StubbedInvocationMatcher stubbedInvocationMatcher1 = mock(StubbedInvocationMatcher.class);
        when(stubbedInvocationMatcher1.getInvocation()).thenReturn(mock(InvocationOnMock.class));
        when(stubbedInvocationMatcher1.answer(mock(InvocationOnMock.class))).thenReturn("Answer1");

        StubbedInvocationMatcher stubbedInvocationMatcher2 = mock(StubbedInvocationMatcher.class);
        when(stubbedInvocationMatcher2.getInvocation()).thenReturn(mock(InvocationOnMock.class));
        when(stubbedInvocationMatcher2.answer(mock(InvocationOnMock.class))).thenReturn("Answer2");

        container.addStubbedInvocation(stubbedInvocationMatcher1);
        container.addStubbedInvocation(stubbedInvocationMatcher2);

        // Create mock handler
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        when(handler.getInvocationContainer()).thenReturn(container);

        // Create invocation
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));

        // Create mock settings
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(mockSettings.getTypeToMock()).thenReturn(mock(Class.class));

        // Create mock util
        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockHandler(mock(Object.class))).thenReturn(handler);

        // Create generic metadata support
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        when(genericMetadataSupport.rawType()).thenReturn(mock(Class.class));

        // Mock actualParameterizedType method
        ReturnsDeepStubs returnsDeepStubsSpy = mock(ReturnsDeepStubs.class);
        when(returnsDeepStubsSpy.actualParameterizedType(mock(Object.class))).thenReturn(genericMetadataSupport);

        // Set up dependencies
        returnsDeepStubsSpy.setMockUtil(mockUtil);

        // Set up mockito mock
        when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(handler);
        when(mockUtil.getMockHandler(mock(Object.class))).thenReturn(handler);
        when(mockUtil.getMockHandler(mockSettings.getTypeToMock())).thenReturn(handler);
        when(mockSettings.getTypeToMock()).thenReturn(mock(Class.class));
        when(handler.getInvocationContainer()).thenReturn(container);

        // Test answer
        Object result = returnsDeepStubsSpy.answer(invocation);

        // Verify mocks and assertions
        assertNull(result);

        // Verify answer method calls
        assertEquals("Answer1", stubbedInvocationMatcher1.answer(invocation));
        assertEquals("Answer2", stubbedInvocationMatcher2.answer(invocation));

        // Verify actualParameterizedType method calls
        assertEquals(genericMetadataSupport, returnsDeepStubsSpy.actualParameterizedType(mock(Object.class)));

        // Verify getMock method calls
        assertEquals("Answer1", returnsDeepStubsSpy.getMock(invocation));
        assertEquals("Answer2", returnsDeepStubsSpy.getMock(invocation));

        // Verify addAnswer method calls
        assertEquals("Answer1", container.getStubbedInvocations().get(0).answer(invocation));
        assertEquals("Answer2", container.getStubbedInvocations().get(1).answer(invocation));
    }

    @Test
    public void testGetMock() throws Throwable {
        // Create mock container
        InvocationContainerImpl container = new InvocationContainerImpl();

        // Create stubbed invocation matchers
        StubbedInvocationMatcher stubbedInvocationMatcher1 = mock(StubbedInvocationMatcher.class);
        when(stubbedInvocationMatcher1.getInvocation()).thenReturn(mock(InvocationOnMock.class));
        when(stubbedInvocationMatcher1.answer(mock(InvocationOnMock.class))).thenReturn("Answer1");

        StubbedInvocationMatcher stubbedInvocationMatcher2 = mock(StubbedInvocationMatcher.class);
        when(stubbedInvocationMatcher2.getInvocation()).thenReturn(mock(InvocationOnMock.class));
        when(stubbedInvocationMatcher2.answer(mock(InvocationOnMock.class))).thenReturn("Answer2");

        container.addStubbedInvocation(stubbedInvocationMatcher1);
        container.addStubbedInvocation(stubbedInvocationMatcher2);

        // Create mock handler
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        when(handler.getInvocationContainer()).thenReturn(container);

        // Create invocation
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));

        // Create mock settings
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(mockSettings.getTypeToMock()).thenReturn(mock(Class.class));

        // Create mock util
        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockHandler(mock(Object.class))).thenReturn(handler);

        // Create generic metadata support
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        when(genericMetadataSupport.rawType()).thenReturn(mock(Class.class));

        // Mock actualParameterizedType method
        ReturnsDeepStubs returnsDeepStubsSpy = mock(ReturnsDeepStubs.class);
        when(returnsDeepStubsSpy.actualParameterizedType(mock(Object.class))).thenReturn(genericMetadataSupport);

        // Set up dependencies
        returnsDeepStubsSpy.setMockUtil(mockUtil);

        // Set up mockito mock
        when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(handler);
        when(mockUtil.getMockHandler(mock(Object.class))).thenReturn(handler);
        when(mockUtil.getMockHandler(mockSettings.getTypeToMock())).thenReturn(handler);
        when(mockSettings.getTypeToMock()).thenReturn(mock(Class.class));
        when(handler.getInvocationContainer()).thenReturn(container);

        // Test getMock method
        Object result = returnsDeepStubsSpy.getMock(invocation);

        // Verify mocks and assertions
        assertNull(result);

        // Verify answer method calls
        assertEquals("Answer1", stubbedInvocationMatcher1.answer(invocation));
        assertEquals("Answer2", stubbedInvocationMatcher2.answer(invocation));

        // Verify actualParameterizedType method calls
        assertEquals(genericMetadataSupport, returnsDeepStubsSpy.actualParameterizedType(mock(Object.class)));

        // Verify getMock method calls
        assertEquals("Answer1", returnsDeepStubsSpy.getMock(invocation));
        assertEquals("Answer2", returnsDeepStubsSpy.getMock(invocation));

        // Verify addAnswer method calls
        assertEquals("Answer1", container.getStubbedInvocations().get(0).answer(invocation));
        assertEquals("Answer2", container.getStubbedInvocations().get(1).answer(invocation));
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        // Create mock container
        InvocationContainerImpl container = new InvocationContainerImpl();

        // Create stubbed invocation matchers
        StubbedInvocationMatcher stubbedInvocationMatcher1 = mock(StubbedInvocationMatcher.class);
        when(stubbedInvocationMatcher1.getInvocation()).thenReturn(mock(InvocationOnMock.class));
        when(stubbedInvocationMatcher1.answer(mock(InvocationOnMock.class))).thenReturn("Answer1");

        container.addStubbedInvocation(stubbedInvocationMatcher1);

        // Create mock handler
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        when(handler.getInvocationContainer()).thenReturn(container);

        // Create invocation
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));

        // Test recordDeepStubMock method
        ReturnsDeepStubs returnsDeepStubsSpy = mock(ReturnsDeepStubs.class);
        Object result = returnsDeepStubsSpy.recordDeepStubMock(invocation, container);

        // Verify mocks and assertions
        assertEquals(result, mock(invocation.getMethod().getReturnType()));

        // Verify answer method calls
        assertEquals("Answer1", stubbedInvocationMatcher1.answer(invocation));
    }

    @Test
    public void testActualParameterizedType() throws Throwable {
        // Create mock container
        InvocationContainerImpl container = new InvocationContainerImpl();

        // Create stubbed invocation matchers
        StubbedInvocationMatcher stubbedInvocationMatcher1 = mock(StubbedInvocationMatcher.class);
        when(stubbedInvocationMatcher1.getInvocation()).thenReturn(mock(InvocationOnMock.class));
        when(stubbedInvocationMatcher1.answer(mock(InvocationOnMock.class))).thenReturn("Answer1");

        container.addStubbedInvocation(stubbedInvocationMatcher1);

        // Create mock handler
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        when(handler.getInvocationContainer()).thenReturn(container);

        // Create invocation
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));

        // Create mock settings
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(mockSettings.getTypeToMock()).thenReturn(mock(Class.class));

        // Create mock util
        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockHandler(mock(Object.class))).thenReturn(handler);

        // Create generic metadata support
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        when(genericMetadataSupport.rawType()).thenReturn(mock(Class.class));

        // Mock actualParameterizedType method
        ReturnsDeepStubs returnsDeepStubsSpy = mock(ReturnsDeepStubs.class);
        when(returnsDeepStubsSpy.actualParameterizedType(mock(Object.class))).thenReturn(genericMetadataSupport);

        // Set up dependencies
        returnsDeepStubsSpy.setMockUtil(mockUtil);

        // Set up mockito mock
        when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(handler);
        when(mockUtil.getMockHandler(mock(Object.class))).thenReturn(handler);
        when(mockUtil.getMockHandler(mockSettings.getTypeToMock())).thenReturn(handler);
        when(mockSettings.getTypeToMock()).thenReturn(mock(Class.class));
        when(handler.getInvocationContainer()).thenReturn(container);

        // Test actualParameterizedType method
        GenericMetadataSupport result = returnsDeepStubsSpy.actualParameterizedType(invocation.getMock());

        // Verify mocks and assertions
        assertEquals(result, genericMetadataSupport);
    }

    private interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {}

    @Test
    public void testGenericsNestMock() {
        GenericsNest<?> mock = mock(GenericsNest.class, returnsDeepStubs);
        Number number = mock.entrySet().iterator().next().getValue().iterator().next();
        assertNull(number);
    }
}