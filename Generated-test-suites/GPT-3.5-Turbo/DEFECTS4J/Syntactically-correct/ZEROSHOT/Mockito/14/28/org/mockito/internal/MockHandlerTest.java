import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class MockHandlerTest {

   private MockHandler<String> mockHandler;
   private MockHandlerInterface<String> oldMockHandler;
   private Invocation invocation;

   @Before
   public void setup() {
      mockHandler = Mockito.mock(MockHandler.class);
      oldMockHandler = Mockito.mock(MockHandlerInterface.class);
      invocation = Mockito.mock(Invocation.class);
   }

   @Test
   public void testHandleWithAnswersForStubbing() throws Throwable {
      when(mockHandler.handle(invocation)).thenReturn(null);

      mockHandler.handle(invocation);

      verify(mockHandler).handle(invocation);
   }

   @Test
   public void testHandleWithoutAnswersForStubbing() throws Throwable {
      when(mockHandler.handle(invocation)).thenReturn(null);
      when(mockHandler.getInvocationContainer().findAnswerFor(invocation)).thenReturn(null);
      when(mockHandler.getMockSettings().getDefaultAnswer().answer(invocation)).thenReturn("SomeAnswer");

      String result = mockHandler.handle(invocation);

      verify(mockHandler).handle(invocation);
      verify(mockHandler).getMockSettings();
      verify(mockHandler).getInvocationContainer().findAnswerFor(invocation);
      verify(mockHandler).getMockSettings().getDefaultAnswer().answer(invocation);
      verify(mockHandler).getInvocationContainer().resetInvocationForPotentialStubbing(any());
   }

   @Test
   public void testVoidMethodStubbable() {
      mockHandler.voidMethodStubbable("mock");

      verify(mockHandler).voidMethodStubbable("mock");
   }

   @Test
   public void testSetAnswersForStubbing() {
      List<Answer> answers = new ArrayList<>();
      mockHandler.setAnswersForStubbing(answers);

      verify(mockHandler).setAnswersForStubbing(answers);
   }

}