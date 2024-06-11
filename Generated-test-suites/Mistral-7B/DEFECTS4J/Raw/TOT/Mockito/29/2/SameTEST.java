import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.Mockito;
import org.hamcrest.Matcher;
import org.hamcrest.Description;
import org.hamcrest.CoreMatchers;

public class SameTest {

	@Test
	public void testSameObjects() {
		Object wantedObject = new Object();
		Same sameMatcher = new Same(wantedObject);

		Object actualObject1 = wantedObject;
		assertTrue(sameMatcher.matches(actualObject1));

		Object actualObject2 = wantedObject;
		assertTrue(sameMatcher.matches(actualObject2));
	}

	@Test
	public void testDifferentObjects() {
		Object wantedObject = new Object();
		Same sameMatcher = new Same(wantedObject);

		Object actualObject1 = new Object();
		assertFalse(sameMatcher.matches(actualObject1));
	}

	@Test
	public void testNullWantedObject() {
		Same sameMatcher = new Same(null);
		assertFalse(sameMatcher.matches(new Object()));
	}

	@Test
	public void testNullActualObject() {
		Object wantedObject = new Object();
		Same sameMatcher = new Same(wantedObject);

		assertFalse(sameMatcher.matches(null));
	}

	@Test
	public void testSameString() {
		String wantedString = "mockito";
		Same sameMatcher = new Same(wantedString);

		String actualString1 = "mockito";
		assertTrue(sameMatcher.matches(actualString1));

		String actualString2 = "mockito";
		assertTrue(sameMatcher.matches(actualString2));
	}

	@Test
	public void testDifferentStrings() {
		String wantedString = "mockito";
		Same sameMatcher = new Same(wantedString);

		String actualString1 = "junit";
		assertFalse(sameMatcher.matches(actualString1));
	}

	@Test
	public void testSameCharacter() {
		Character wantedCharacter = 'a';
		Same sameMatcher = new Same(wantedCharacter);

		Character actualCharacter1 = 'a';
		assertTrue(sameMatcher.matches(actualCharacter1));

		Character actualCharacter2 = 'a';
		assertTrue(sameMatcher.matches(actualCharacter2));
	}

	@Test
	public void testDifferentCharacters() {
		Character wantedCharacter = 'a';
		Same sameMatcher = new Same(wantedCharacter);

		Character actualCharacter1 = 'b';
		assertFalse(sameMatcher.matches(actualCharacter1));
	}

	@Test
	public void testMockitoMock() {
		Object wantedMock = Mockito.mock(Object.class);
		Same sameMatcher = new Same(wantedMock);

		Object actualMock = Mockito.mock(Object.class);
		assertTrue(sameMatcher.matches(actualMock));
	}

	@Test
	public void testDescription() {
		Object wantedObject = new Object();
		Same sameMatcher = new Same(wantedObject);

		Description description = Mockito.mock(Description.class);
		sameMatcher.describeTo(description);

		Mockito.verify(description, Mockito.times(1)).appendText("same(");
		Mockito.verify(description, Mockito.times(1)).appendText("\"");
		Mockito.verify(description, Mockito.times(1)).appendText(wantedObject.toString());
		Mockito.verify(description, Mockito.times(1)).appendText("\")");
	}
}