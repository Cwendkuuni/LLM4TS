package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.verification.VerificationMode;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class MockHandlerTest {
    private MockHandler mockHandler;
    private MockSettingsImpl mockSettings;
    private InvocationContainerImpl invocationContainerImpl;

    @Before
    public void setup() {
        mockSettings = new MockSettingsImpl();
        mockHandler = new MockHandler(mockSettings);
        invocationContainerImpl = new InvocationContainerImpl(mockHandler.mockingProgress);
        mockHandler.invocationContainerImpl = invocationContainerImpl;
    }

    @Test
    public void testHandle() throws Throwable {
        Invocation invocation = new Invocation() {
            public Object getMock() {
                return null;
            }

            public List<Object> getArguments() {
                return null;
            }

            public Object callRealMethod() throws Throwable {
                return null;
            }

            public boolean isVerified() {
                return false;
            }

            public Object getRawArguments() {
                return null;
            }

            public String toString() {
                return null;
            }

            public InvocationMatcher getInvocationMatcher() {
                return null;
            }

            public void markVerified() {
            }
        };
        mockHandler.handle(invocation);
    }

    @Test
    public void testHandle_withInvocationContainerImplHasAnswersForStubbing() throws Throwable {
        Invocation invocation = new Invocation() {
            public Object getMock() {
                return null;
            }

            public List<Object> getArguments() {
                return null;
            }

            public Object callRealMethod() throws Throwable {
                return null;
            }

            public boolean isVerified() {
                return false;
            }

            public Object getRawArguments() {
                return null;
            }

            public String toString() {
                return null;
            }

            public InvocationMatcher getInvocationMatcher() {
                return null;
            }

            public void markVerified() {
            }
        };
        
        Answer answer = new Answer() {
			public Object answer(InvocationOnMock invocation) throws Throwable {
				return null;
			}
		};
		
		List<Answer> answers = new ArrayList<>();
		answers.add(answer);
		mockHandler.invocationContainerImpl.setAnswersForStubbing(answers);
		
        mockHandler.handle(invocation);
    }

    @Test
    public void testHandle_withVerificationMode() throws Throwable {
        VerificationMode verificationMode = new VerificationMode() {
            public void verify(VerificationData data) {
            }
        };
        mockHandler.mockingProgress.reportingMode = verificationMode;
        Invocation invocation = new Invocation() {
            public Object getMock() {
                return null;
            }

            public List<Object> getArguments() {
                return null;
            }

            public Object callRealMethod() throws Throwable {
                return null;
            }

            public boolean isVerified() {
                return false;
            }

            public Object getRawArguments() {
                return null;
            }

            public String toString() {
                return null;
            }

            public InvocationMatcher getInvocationMatcher() {
                return null;
            }

            public void markVerified() {
            }
        };
        mockHandler.handle(invocation);
    }

    @Test
    public void testHandle_withStubbedInvocation() throws Throwable {
        Invocation invocation = new Invocation() {
            public Object getMock() {
                return null;
            }

            public List<Object> getArguments() {
                return null;
            }

            public Object callRealMethod() throws Throwable {
                return null;
            }

            public boolean isVerified() {
                return false;
            }

            public Object getRawArguments() {
                return null;
            }

            public String toString() {
                return null;
            }

            public InvocationMatcher getInvocationMatcher() {
                return null;
            }

            public void markVerified() {
            }
        };

        StubbedInvocationMatcher stubbedInvocation = new StubbedInvocationMatcher(null, null, null);
        when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        
        mockHandler.handle(invocation);
        
        verify(stubbedInvocation).captureArgumentsFrom(invocation);
        verify(stubbedInvocation).answer(invocation);
    }

    @Test
    public void testHandle_withUnstubbedInvocation() throws Throwable {
        Invocation invocation = new Invocation() {
            public Object getMock() {
                return null;
            }

            public List<Object> getArguments() {
                return null;
            }

            public Object callRealMethod() throws Throwable {
                return null;
            }

            public boolean isVerified() {
                return false;
            }

            public Object getRawArguments() {
                return null;
            }

            public String toString() {
                return null;
            }

            public InvocationMatcher getInvocationMatcher() {
                return null;
            }

            public void markVerified() {
            }
        };

        Answer answer = new Answer() {
			public Object answer(InvocationOnMock invocation) throws Throwable {
				return null;
			}
		};
		
		when(mockSettings.getDefaultAnswer().answer(invocation)).thenReturn(answer);
		StubbedInvocationMatcher stubbedInvocation = new StubbedInvocationMatcher(null, null, null);
        when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null, stubbedInvocation);

        mockHandler.handle(invocation);
        
        verify(stubbedInvocation).captureArgumentsFrom(invocation);
        verify(mockHandler.invocationContainerImpl).resetInvocationForPotentialStubbing(null);
        verify(mockSettings.getDefaultAnswer()).answer(invocation);
    }

    @Test
    public void testVoidMethodStubbable() {
        T mock = null;
        VoidMethodStubbable stubbable = mockHandler.voidMethodStubbable(mock);
        assertNull(stubbable);
    }

    @Test
    public void testGetMockSettings() {
        assertEquals(mockSettings, mockHandler.getMockSettings());
    }

    @Test
    public void testSetAnswersForStubbing() {
        List answers = new ArrayList();
        mockHandler.setAnswersForStubbing(answers);
        verify(mockHandler.invocationContainerImpl).setAnswersForStubbing(answers);
    }

    @Test
    public void testGetInvocationContainer() {
        assertEquals(mockHandler.invocationContainerImpl, mockHandler.getInvocationContainer());
    }
}