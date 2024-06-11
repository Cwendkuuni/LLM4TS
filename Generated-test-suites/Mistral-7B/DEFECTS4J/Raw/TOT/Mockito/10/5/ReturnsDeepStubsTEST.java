package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockSettings;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswerWithDeepStub() {
        InterfaceWithNestedGenerics<String, Number> interfaceWithNestedGenerics = Mockito.mock(InterfaceWithNestedGenerics.class, new ReturnsDeepStubs());

        Number number = interfaceWithNestedGenerics.getMap().keySet().iterator().next().getValue().iterator().next();

        Assertions.assertNotNull(number);
    }

    @Test
    public void testAnswerWithNonMockableType() {
        InterfaceWithNestedGenerics<String, Number> interfaceWithNestedGenerics = Mockito.mock(InterfaceWithNestedGenerics.class, new ReturnsDeepStubs());

        String string = interfaceWithNestedGenerics.getMap().keySet().iterator().next().getValue();

        Assertions.assertNotNull(string);
    }

    @Test
    public void testAnswerWithRecordedDeepStub() throws Exception {
        InterfaceWithNestedGenerics<String, Number> interfaceWithNestedGenerics = Mockito.mock(InterfaceWithNestedGenerics.class, new ReturnsDeepStubs());
        Mockito.doReturn(newDeepStubMock(Map.class)).when(interfaceWithNestedGenerics).getMap();

        InterfaceWithNestedGenerics<String, Number> deepStub = interfaceWithNestedGenerics;
        Number number = deepStub.getMap().keySet().iterator().next().getValue().iterator().next();

        Assertions.assertNotNull(number);

        //Verify that the answer was recorded
        verify(deepStub).getMap();
        verify(interfaceWithNestedGenerics).getMap();
    }

    @Test
    public void testAnswerWithDeepStubInNestedInvocation() throws Exception {
        InterfaceWithNestedGenerics<String, InterfaceWithNestedGenerics<String, Number>> interfaceWithNestedGenerics = Mockito.mock(InterfaceWithNestedGenerics.class, new ReturnsDeepStubs());

        InterfaceWithNestedGenerics<String, Number> nestedInterfaceWithNestedGenerics = interfaceWithNestedGenerics.getNestedInterface();
        Number number = nestedInterfaceWithNestedGenerics.getMap().keySet().iterator().next().getValue().iterator().next();

        Assertions.assertNotNull(number);
    }

    @Test
    public void testAnswerWithError() throws Exception {
        InterfaceWithNestedGenerics<String, Number> interfaceWithNestedGenerics = Mockito.mock(InterfaceWithNestedGenerics.class, new ReturnsDeepStubs());

        try {
            interfaceWithNestedGenerics.throwException();
        } catch (Exception e) {
            Assertions.assertInstanceOf(RuntimeException.class, e);
            Assertions.assertEquals("Test Exception", e.getMessage());
        }
    }

    @Test
    public void testAnswerWithNonSerializableDeepStub() throws Exception {
        InterfaceWithNestedGenerics<String, NonSerializableDeepStub> interfaceWithNestedGenerics = Mockito.mock(InterfaceWithNestedGenerics.class, new ReturnsDeepStubs());

        NonSerializableDeepStub nonSerializableDeepStub = interfaceWithNestedGenerics.getMap().keySet().iterator().next().getValue();

        try {
            Mockito.serialize(nonSerializableDeepStub);
            Assertions.fail("Expected SerializationException");
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof SerializationException);
        }
    }

    private Object newDeepStubMock(Class<?> clazz) {
        return Mockito.mock(clazz, withSettings().defaultAnswer(new ReturnsDeepStubs()));
    }

    interface InterfaceWithNestedGenerics<K extends Comparable<K> & Cloneable, V extends Number & Comparable<V>> extends Map<K, Set<V>> {}

    interface NestedInterfaceWithNestedGenerics<K extends Comparable<K> & Cloneable, V extends Number & Comparable<V>> extends Map<K, Set<V>> {
        NestedInterfaceWithNestedGenerics<K, V> getNestedInterface();
    }

    static class NonSerializableDeepStub implements Serializable {
        private static final long serialVersionUID = -1234567890123456789L;

        public String toString() {
            return "NonSerializableDeepStub";
        }

        @SuppressWarnings("unchecked")
        private Object readResolve() throws ObjectStreamException {
            return Mockito.RETURNS_DEEP_STUBS;
        }
    }

    static class SerializationException extends Exception {
        public SerializationException(String message) {
            super(message);
        }
    }
}