package org.junit.jupiter.api;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class MockHandlerTest {

// ... (previous test methods)

@Test
public void testHandleWithStub() {
    when(mockHandler).thenCallRealMethod();
    Invocation invocation = mock(Invocation.class);
    Object result = mockHandler.handle(invocation);
    verify(mockHandler, times(1)).handle(invocation);
    verify(mockHandler, atLeastOnce()).handle(any(Invocation.class));
    assertEquals(null, result);
}

// ... (other test methods)

}