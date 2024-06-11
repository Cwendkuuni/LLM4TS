import org.junit.*;
import static org.junit.Assert.*;
import jigl.image.*;
import jigl.image.exceptions.*;

public class ShiftTest {
	
	@Test
	public void testApply_GrayImage() {
		GrayImage image = new GrayImage(4, 4);
		image.set(0, 0, 1);
		image.set(1, 1, 2);
		image.set(2, 2, 3);
		
		Shift shift = new Shift(1, 1, Shift.NOWRAP);
		GrayImage result = shift.apply(image);
		
		GrayImage expected = new GrayImage(4, 4);
		expected.set(1, 1, 1);
		expected.set(2, 2, 2);
		expected.set(3, 3, 3);
		
		assertEquals(expected, result);
	}
	
	@Test
	public void testApply_GrayImage_Wrap() {
		GrayImage image = new GrayImage(4, 4);
		image.set(0, 0, 1);
		image.set(1, 1, 2);
		image.set(2, 2, 3);
		
		Shift shift = new Shift(1, 1, Shift.WRAP);
		GrayImage result = shift.apply(image);
		
		GrayImage expected = new GrayImage(4, 4);
		expected.set(1, 1, 2);
		expected.set(2, 2, 3);
		expected.set(3, 3, 1);
		
		assertEquals(expected, result);
	}
	
	@Test
	public void testApply_RealGrayImage() {
		RealGrayImage image = new RealGrayImage(4, 4);
		image.set(0, 0, 1.0f);
		image.set(1, 1, 2.0f);
		image.set(2, 2, 3.0f);
		
		Shift shift = new Shift(1, 1, Shift.NOWRAP);
		RealGrayImage result = shift.apply(image);
		
		RealGrayImage expected = new RealGrayImage(4, 4);
		expected.set(1, 1, 1.0f);
		expected.set(2, 2, 2.0f);
		expected.set(3, 3, 3.0f);
		
		assertEquals(expected, result);
	}
	
	@Test
	public void testApply_RealGrayImage_Wrap() {
		RealGrayImage image = new RealGrayImage(4, 4);
		image.set(0, 0, 1.0f);
		image.set(1, 1, 2.0f);
		image.set(2, 2, 3.0f);
		
		Shift shift = new Shift(1, 1, Shift.WRAP);
		RealGrayImage result = shift.apply(image);
		
		RealGrayImage expected = new RealGrayImage(4, 4);
		expected.set(1, 1, 2.0f);
		expected.set(2, 2, 3.0f);
		expected.set(3, 3, 1.0f);
		
		assertEquals(expected, result);
	}
	
	@Test
	public void testApply_ColorImage() {
		ColorImage image = new ColorImage(4, 4);
		image.setPlane(0, new GrayImage(4, 4));
		image.setPlane(1, new GrayImage(4, 4));
		image.setPlane(2, new GrayImage(4, 4));
		image.set(0, 0, 1, 1);
		
		Shift shift = new Shift(1, 1, Shift.NOWRAP);
		ColorImage result = shift.apply(image);
		
		ColorImage expected = new ColorImage(4, 4);
		expected.setPlane(0, new GrayImage(4, 4));
		expected.setPlane(1, new GrayImage(4, 4));
		expected.setPlane(2, new GrayImage(4, 4));
		expected.set(1, 1, 1, 1);
		
		assertEquals(expected, result);
	}
	
	@Test
	public void testApply_ColorImage_Wrap() {
		ColorImage image = new ColorImage(4, 4);
		image.setPlane(0, new GrayImage(4, 4));
		image.setPlane(1, new GrayImage(4, 4));
		image.setPlane(2, new GrayImage(4, 4));
		image.set(0, 0, 1, 1);
		
		Shift shift = new Shift(1, 1, Shift.WRAP);
		ColorImage result = shift.apply(image);
		
		ColorImage expected = new ColorImage(4, 4);
		expected.setPlane(0, new GrayImage(4, 4));
		expected.setPlane(1, new GrayImage(4, 4));
		expected.setPlane(2, new GrayImage(4, 4));
		expected.set(1, 1, 1, 1);
		
		assertEquals(expected, result);
	}
	
}