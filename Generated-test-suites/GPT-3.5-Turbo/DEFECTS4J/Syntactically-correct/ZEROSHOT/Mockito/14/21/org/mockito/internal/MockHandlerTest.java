import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.stubbing.Answer;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.*;

public class MockHandlerTest {

   private MockHandler mockHandler;

   @Before
   public void setUp() {
      mockHandler = new MockHandler();
   }

   @Test
   public void testHandle() throws Throwable {
      Invocation invocation = mock(Invocation.class);
      InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
      when(mockHandler.getMockSettings().getDefaultAnswer().answer(invocation)).thenReturn("Mocked Answer");

      Object result = mockHandler.handle(invocation);

      verify(mockHandler.getMockSettings(), times(1)).getDefaultAnswer();
      assertNotNull(result);
      assertEquals("Mocked Answer", result);
   }

   @Test
   public void testVoidMethodStubbable() {
      MockHandlerInterface mockHandlerInterface = mock(MockHandlerInterface.class);
      VoidMethodStubbable mockVoidMethodStubbable = mock(VoidMethodStubbable.class);

      when(mockHandlerInterface.voidMethodStubbable(any())).thenReturn(mockVoidMethodStubbable);
      VoidMethodStubbable result = mockHandlerInterface.voidMethodStubbable(this.mockHandler);

      assertNotNull(result);
      assertEquals(mockVoidMethodStubbable, result);
   }

   @Test
   public void testGetMockSettings() {
      MockSettingsImpl mockSettings = new MockSettingsImpl();
      mockHandler = new MockHandler(mockSettings);

      MockSettingsImpl result = mockHandler.getMockSettings();

      assertEquals(mockSettings, result);
   }

   @Test
   public void testSetAnswersForStubbing() {
      List<Answer> answers = mock(List.class);

      mockHandler.setAnswersForStubbing(answers);

      assertEquals(answers, mockHandler.getInvocationContainer().getAnswersForStubbing());
   }
}