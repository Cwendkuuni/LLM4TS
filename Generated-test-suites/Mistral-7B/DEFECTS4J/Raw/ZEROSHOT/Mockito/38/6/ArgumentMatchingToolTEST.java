import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Stubber;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.nullValue;

@RunWith(MockitoJUnitRunner.class)
public class ArgumentMatchingToolTest {

 private ArgumentMatchingTool tool;
 private Stubber stubber;

 @Before
 public void setUp() {
 tool = new ArgumentMatchingTool();
 stubber = stubber(Stubber.class);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_ValidMatchers_ReturnsEmptyArray() {
 // Given
 List<Matcher> matchers = Arrays.asList(isA(Integer.class), isA(String.class));
 Object[] arguments = new Object[]{1, "test"};

 // When
 Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

 // Then
 assertThat(indexes, is(new Integer[0]));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_MismatchedSize_ReturnsEmptyArray() {
 // Given
 List<Matcher> matchers = Arrays.asList(isA(Integer.class));
 Object[] arguments = new Object[]{1, "test"};

 // When
 Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

 // Then
 assertThat(indexes, is(new Integer[0]));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NoSuspiciousMatchers_ReturnsEmptyArray() {
 // Given
 List<Matcher> matchers = Arrays.asList(isA(Integer.class), isA(Integer.class));
 Object[] arguments = new Object[]{1, 1};

 // When
 Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

 // Then
 assertThat(indexes, is(new Integer[0]));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_SuspiciouslyNotMatchingArgument_ReturnsIndex() {
 // Given
 List<Matcher> matchers = Arrays.asList(contains("test"), isA(String.class));
 Object[] arguments = new Object[]{"test1", "test"};

 // When
 Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

 // Then
 assertThat(indexes, is(new Integer[]{0}));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_SuspiciouslyNotMatchingArgumentWithType_ReturnsIndex() {
 // Given
 List<Matcher> matchers = Arrays.asList(instanceOf(StringBuilder.class), isA(String.class));
 Object[] arguments = new Object[]{new StringBuilder(), "test"};

 // When
 Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

 // Then
 assertThat(indexes, is(new Integer[]{0}));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_SuspiciouslyNotMatchingArguments_ReturnsMultipleIndices() {
 // Given
 List<Matcher> matchers = Arrays.asList(contains("test"), contains("tester"), isA(String.class));
 Object[] arguments = new Object[]{"test1", "test2", "test"};

 // When
 Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

 // Then
 List<Integer> expectedIndexes = new LinkedList<>();
 expectedIndexes.add(0);
 expectedIndexes.add(1);
 assertThat(indexes, is(equalTo(expectedIndexes.toArray(new Integer[0]))));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NullMatcher_ThrowsNullPointerException() {
 // Given
 List<Matcher> matchers = Arrays.asList(null);
 Object[] arguments = new Object[]{1, "test"};

 // When & Then
 expectThrown(NullPointerException.class, () -> tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NullArguments_ThrowsNullPointerException() {
 // Given
 List<Matcher> matchers = Arrays.asList(isA(Integer.class));
 Object[] arguments = null;

 // When & Then
 expectThrown(NullPointerException.class, () -> tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_EmptyMatchers_ThrowsNullPointerException() {
 // Given
 List<Matcher> matchers = new LinkedList<>();
 Object[] arguments = new Object[]{1, "test"};

 // When & Then
 expectThrown(NullPointerException.class, () -> tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }

 private Matcher contains(String value) {
 return new ContainsString(value);
 }

 private Stubber stubber(Class<?> clazz) {
 return stubber.createStub(clazz);
 }
}