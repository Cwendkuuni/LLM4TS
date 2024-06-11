package org.jsoup.safety.test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Whitelist;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testCleanMethod() {
        // Given
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        String dirtyHtml = "<html><body><script>alert('XSS')</script><div>Valid content</div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        Document expectedCleanDocument = Jsoup.parse("<html><body><div>Valid content</div></body></html>");

        // When
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Then
        assertEquals(expectedCleanDocument.body().html(), cleanDocument.body().html());
        assertNotSame(dirtyDocument, cleanDocument);
    }

    @Test
    public void testIsValidMethod() {
        // Given
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        String validHtml = "<html><body><div>Valid content</div></body></html>";
        Document validDocument = Jsoup.parse(validHtml);
        String invalidHtml = "<html><body><script>alert('XSS')</script></body></html>";
        Document invalidDocument = Jsoup.parse(invalidHtml);

        // When
        boolean isValidValid = cleaner.isValid(validDocument);
        boolean isInvalidInvalid = cleaner.isValid(invalidDocument);

        // Then
        assertTrue(isValidValid);
        assertFalse(isInvalidInvalid);
    }

    @Test
    public void testCleanEmptyDocument() {
        // Given
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Document.createEmpty();

        // When
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Then
        assertEquals(cleanDocument.body().text(), "");
        assertNotNull(cleanDocument.body());
    }

    @Test
    public void testIsValidEmptyDocument() {
        // Given
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Document.createEmpty();

        // When
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Then
        assertTrue(isValid);
    }

    @Test
    public void testCleanDisallowedTag() {
        // Given
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        String dirtyHtml = "<html><body><script>alert('XSS')</script><div>Valid content</div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        Document expectedCleanDocument = Jsoup.parse("<html><body><div>Valid content</div></body></html>");

        // When
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Then
        assertEquals(expectedCleanDocument.body().html(), cleanDocument.body().html());
        assertNotSame(dirtyDocument, cleanDocument);
    }

    @Test
    public void testIsValidDisallowedTag() {
        // Given
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        String dirtyHtml = "<html><body><script>alert('XSS')</script><div>Valid content</div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        // When
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Then
        assertFalse(isValid);
    }

    // Add edge cases and exception handling tests here
}