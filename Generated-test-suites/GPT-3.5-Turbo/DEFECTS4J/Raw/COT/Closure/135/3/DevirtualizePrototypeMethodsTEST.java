
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  DevirtualizePrototypeMethodsTest.testIsCall.class,
  DevirtualizePrototypeMethodsTest.testIsPrototypeMethodDefinition.class,
  DevirtualizePrototypeMethodsTest.testGetRewrittenMethodName.class,
  DevirtualizePrototypeMethodsTest.testIsEligibleDefinition.class,
  DevirtualizePrototypeMethodsTest.testRewriteCallSites.class,
  DevirtualizePrototypeMethodsTest.testRewriteDefinition.class,
  DevirtualizePrototypeMethodsTest.testFixFunctionType.class,
  DevirtualizePrototypeMethodsTest.testReplaceReferencesToThis.class,
  // Add test cases for identified edge cases and exceptions
})
public class DevirtualizePrototypeMethodsTest {
}
