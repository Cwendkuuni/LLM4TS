import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.ExactAnswer;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerExecutor;
import org.mockito.plugins.MockMakerUtil;
import org.mockito.plugins.MockMakerUtil.DefaultMockMaker;
import org.mockito.plugins.MockMakerUtil.StubbedType;
import org.mockito.quality.Danger;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockMakerJUnitRunner.class)
public class ByteBuddyMockMakerTest {

 private ByteBuddyMockMaker byteBuddyMockMaker;
 private MockMaker mockMaker;

 @Before
 public void setUp() {
 byteBuddyMockMaker = new ByteBuddyMockMaker();
 mockMaker = new DefaultMockMaker();
 }

 @Test
 public void testCreateMockSuccess() {
 Class<MockitoTestClass> mockedType = MockitoTestClass.class;
 MockHandler mockHandler = mock(MockHandler.class);

 StubbedType<MockitoTestClass> stubbedType = MockMakerUtil.makeStubbedType(mockedType, MockMaker.class, byteBuddyMockMaker);
 MockitoTestClass mock = byteBuddyMockMaker.createMock(new MockCreationSettings<>(mockedType, mockHandler), null);

 verify(mockHandler).handle(Mockito.any(), Mockito.eq(mockedType));
 }

 @Test(expected = MockitoException.class)
 public void testCreateMockFailureWithSerializationAcrossClassloaders() {
 Class<MockitoTestClass> mockedType = MockitoTestClass.class;
 MockHandler mockHandler = mock(MockHandler.class);

 StubbedType<MockitoTestClass> stubbedType = MockMakerUtil.makeStubbedType(mockedType, MockMaker.class, byteBuddyMockMaker);
 byteBuddyMockMaker.createMock(new MockCreationSettings<>(mockedType, mockHandler, SerializableMode.ACROSS_CLASSLOADERS), null);
 }

 @Test
 public void testGetHandler() {
 MockitoTestClass mock = mock(MockitoTestClass.class, new MockCreationSettings<>(MockitoTestClass.class, null));
 MockHandler handler = byteBuddyMockMaker.getHandler(mock);

 ArgumentCaptor<InvocationOnMock> invocationCaptor = ArgumentCaptor.forClass(InvocationOnMock.class);
 doAnswer(new ExactAnswer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) {
 return "MockHandler Answered";
 }
 }).when(handler).handle(invocationCaptor.capture());

 byteBuddyMockMaker.resetMock(mock, mockHandler, new MockCreationSettings<>(MockitoTestClass.class, null));
 verify(handler).handle(invocationCaptor.getValue());
 }

 @Test
 public void testResetMock() {
 MockitoTestClass mock = mock(MockitoTestClass.class, new MockCreationSettings<>(MockitoTestClass.class, null));
 MockHandler mockHandler = mock(MockHandler.class);
 byteBuddyMockMaker.resetMock(mock, mockHandler, new MockCreationSettings<>(MockitoTestClass.class, null));

 ArgumentCaptor<MockHandler> handlerCaptor = ArgumentCaptor.forClass(MockHandler.class);
 verify(mock).setMockitoInterceptor(Mockito.eq(Mockito.notNull()));
 verify(mock).setMockitoInterceptor(handlerCaptor.capture());

 Mockito.verifyNoMoreInteractions(mockHandler);
 Mockito.verifyNoMoreInteractions(mock);
 }

 @Test
 public void testInitializeClassInstantiator() {
 try {
 byteBuddyMockMaker.initializeClassInstantiator();
 } catch (Exception e) {
 throw new Danger.Exception("Failed to initialize class instantiator", e);
 }
 }
}