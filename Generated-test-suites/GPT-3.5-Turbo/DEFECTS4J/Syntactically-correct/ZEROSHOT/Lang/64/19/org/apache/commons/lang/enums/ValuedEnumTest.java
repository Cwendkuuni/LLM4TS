import org.junit.Assert;
import org.junit.Test;

public class ValuedEnumTest {

	@Test
	public void testGetEnum() {
		JavaVersionEnum java1 = JavaVersionEnum.getEnum(100);
		Assert.assertEquals("Java 1.0", java1.getName());

		JavaVersionEnum java2 = JavaVersionEnum.getEnum("Java 1.2");
		Assert.assertEquals(120, java2.getValue());
		
		JavaVersionEnum java3 = JavaVersionEnum.getEnum(150);
		Assert.assertNull(java3);
	}
	
	@Test
	public void testGetValue() {
		JavaVersionEnum java1 = JavaVersionEnum.getEnum(100);
		Assert.assertEquals(100, java1.getValue());
		
		JavaVersionEnum java2 = JavaVersionEnum.getEnum("Java 1.2");
		Assert.assertEquals(120, java2.getValue());
	}
	
	@Test
	public void testCompareTo() {
		JavaVersionEnum java1 = JavaVersionEnum.getEnum(100);
		JavaVersionEnum java2 = JavaVersionEnum.getEnum("Java 1.2");
		
		Assert.assertTrue(java1.compareTo(java2) < 0);
		Assert.assertTrue(java2.compareTo(java1) > 0);
		Assert.assertTrue(java1.compareTo(java1) == 0);
	}
	
	@Test
	public void testToString() {
		JavaVersionEnum java1 = JavaVersionEnum.getEnum(100);
		Assert.assertEquals("JavaVersion[Java 1.0=100]", java1.toString());
		
		JavaVersionEnum java2 = JavaVersionEnum.getEnum("Java 1.2");
		Assert.assertEquals("JavaVersion[Java 1.2=120]", java2.toString());
	}
}