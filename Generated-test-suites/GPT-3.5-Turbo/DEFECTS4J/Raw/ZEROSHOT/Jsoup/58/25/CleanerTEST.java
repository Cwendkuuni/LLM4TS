import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class CleanerTest {
    
    private Cleaner cleaner;
    private Whitelist whitelist;
    
    @Before
    public void setup() {
        whitelist = mock(Whitelist.class);
        cleaner = new Cleaner(whitelist);
    }
    
    @Test
    public void testClean() {
        Document dirtyDocument = mock(Document.class);
        Document cleanDocument = mock(Document.class);
        
        when(dirtyDocument.baseUri()).thenReturn("http://example.com");
        when(dirtyDocument.body()).thenReturn(mock(Element.class));
        when(Document.createShell("http://example.com")).thenReturn(cleanDocument);
        
        cleaner.clean(dirtyDocument);
        
        verify(dirtyDocument).baseUri();
        verify(dirtyDocument).body();
        verify(cleanDocument).body();
    }
    
    @Test
    public void testIsValid() {
        Document dirtyDocument = mock(Document.class);
        Document cleanDocument = mock(Document.class);
        
        when(dirtyDocument.baseUri()).thenReturn("http://example.com");
        when(dirtyDocument.body()).thenReturn(mock(Element.class));
        when(Document.createShell("http://example.com")).thenReturn(cleanDocument);
        
        cleaner.isValid(dirtyDocument);
        
        verify(dirtyDocument).baseUri();
        verify(dirtyDocument).body();
        verify(cleanDocument).body();
    }
}