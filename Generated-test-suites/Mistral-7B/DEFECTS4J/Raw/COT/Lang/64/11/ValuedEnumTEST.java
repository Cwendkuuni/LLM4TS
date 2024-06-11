import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

 private static final Class<ValuedEnum> VALUED_ENUM_CLASS = ValuedEnum.class;

 @Test
 public void testGetEnumByName() {
 ValuedEnum javaVersion1_0 = ValuedEnum.getEnum("JavaVersionEnum", "Java 1.0");
 assertNotNull(javaVersion1_0);
 assertEquals("Java 1.0", javaVersion1_0.getName());
 assertEquals(100, javaVersion1_0.getValue());
 }

 @Test
 public void testGetEnumByValue() {
 ValuedEnum javaVersion1_0 = ValuedEnum.getEnum(VALUED_ENUM_CLASS, 100);
 assertNotNull(javaVersion1_0);
 assertEquals("Java 1.0", javaVersion1_0.getName());
 }

 @Test
 public void testGetEnumMap() {
 List<ValuedEnum> javaVersionList = ValuedEnum.getEnumList(VALUED_ENUM_CLASS);
 Map<Integer, ValuedEnum> javaVersionMap = ValuedEnum.getEnumMap(VALUED_ENUM_CLASS);

 for (ValuedEnum javaVersion : javaVersionList) {
 assertEquals(javaVersion, javaVersionMap.get(javaVersion.getValue()));
 }
 }

 @Test
 public void testCompareValues() {
 ValuedEnum javaVersion1_0 = new ValuedEnum("JavaVersion", 100);
 ValuedEnum javaVersion1_1 = new ValuedEnum("JavaVersion", 110);

 int comparisonResult = javaVersion1_0.compareTo(javaVersion1_1);
 assertEquals(-1, comparisonResult);
 }

 @Test(expected = NullPointerException.class)
 public void testNullEnumArgument() {
 ValuedEnum.getEnum(null, 100);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeEnumValue() {
 ValuedEnum.getEnum(VALUED_ENUM_CLASS, -1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullEnumName() {
 ValuedEnum.getEnum(VALUED_ENUM_CLASS, null);
 }

 @Test(expected = ClassCastException.class)
 public void testCompareToWithDifferentClass() throws Exception {
 ValuedEnum javaVersion1_0 = new ValuedEnum("JavaVersion", 100);
 Object javaVersion2_0 = new Object() {
 @Override
 public int compareTo(Object o) {
 return 1;
 }
 };
 int comparisonResult = javaVersion1_0.compareTo(javaVersion2_0);
 fail("Comparison with a different class should fail.");
 }

 @Test
 public void testToString() {
 ValuedEnum javaVersion1_0 = new ValuedEnum("JavaVersion", 100);
 assertEquals("JavaVersion[Java 1.0=100]", javaVersion1_0.toString());
 }
}