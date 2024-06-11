import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CharacterReader.class, Tokeniser.class, Validate.class})
public class TreeBuilderTest {
    
    private TreeBuilder treeBuilder;
    private CharacterReader characterReader;
    private Tokeniser tokeniser;
    private Document document;
    private ArrayList<Element> stack;
    
    @Before
    public void setUp() {
        PowerMockito.mockStatic(CharacterReader.class);
        PowerMockito.mockStatic(Tokeniser.class);
        PowerMockito.mockStatic(Validate.class);
        
        characterReader = mock(CharacterReader.class);
        Mockito.when(CharacterReader.class, "new", anyString()).thenReturn(characterReader);
        
        tokeniser = mock(Tokeniser.class);
        Mockito.when(Tokeniser.class, "new", characterReader, null).thenReturn(tokeniser);
        
        document = mock(Document.class);
        Mockito.when(Document.class, "new", anyString()).thenReturn(document);
        
        stack = mock(ArrayList.class);
        stack = PowerMockito.spy(new ArrayList<Element>());
        
        PowerMockito.when(stack, "size").thenReturn(mock(int.class));
        
        treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        
        treeBuilder.doc = document;
        treeBuilder.stack = stack;
    }
    
    @Test
    public void testInitialiseParse() {
        String input = "input";
        String baseUri = "baseUri";
        ParseErrorList errors = null;
        
        treeBuilder.initialiseParse(input, baseUri, errors);
        
        PowerMockito.verifyStatic(CharacterReader.class);
        CharacterReader.newInstance(input);
        
        PowerMockito.verifyStatic(Validate.class);
        Validate.notNull(input, "String input must not be null");
        
        PowerMockito.verifyStatic(Validate.class);
        Validate.notNull(baseUri, "BaseURI must not be null");
        
        PowerMockito.verifyStatic(Document.class);
        Document.newInstance(baseUri);
        
        Mockito.verify(treeBuilder, times(1)).runParser();
    }
    
    @Test
    public void testParse() {
        String input = "input";
        String baseUri = "baseUri";
        
        treeBuilder.parse(input, baseUri);
        
        Mockito.verify(treeBuilder, times(1)).initialiseParse(input, baseUri, ParseErrorList.noTracking());
    }
    
    @Test
    public void testParseWithErrors() {
        String input = "input";
        String baseUri = "baseUri";
        ParseErrorList errors = mock(ParseErrorList.class);
        
        treeBuilder.parse(input, baseUri, errors);
        
        Mockito.verify(treeBuilder, times(1)).initialiseParse(input, baseUri, errors);
    }
    
    @Test
    public void testRunParser() {
        Token token = mock(Token.class);
        when(tokeniser.read()).thenReturn(token).thenReturn(new Token(Token.TokenType.EOF));
        when(token.type()).thenReturn(Token.TokenType.END_TAG);
        
        treeBuilder.runParser();
        
        Mockito.verify(treeBuilder, times(2)).process(token);
        
        Mockito.verify(token, times(2)).reset();
        
        verify(token, times(1)).type();
    }
    
    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = PowerMockito.spy(treeBuilder.start);
        when(treeBuilder.process(startTag)).thenReturn(true);
        
        boolean result = treeBuilder.processStartTag("name");
        
        verify(startTag, times(1)).reset();
        verify(startTag, times(1)).name("name");
        
        Mockito.verify(treeBuilder, times(1)).process(startTag);
        assert(result);
    }
    
    @Test
    public void testProcessStartTagWithAttributes() {
        Token.StartTag startTag = PowerMockito.spy(treeBuilder.start);
        Attributes attrs = mock(Attributes.class);
        when(treeBuilder.process(startTag)).thenReturn(true);
        
        boolean result = treeBuilder.processStartTag("name", attrs);
        
        verify(startTag, times(1)).reset();
        verify(startTag, times(1)).nameAttr("name", attrs);
        
        Mockito.verify(treeBuilder, times(1)).process(startTag);
        assert(result);
    }
    
    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = PowerMockito.spy(treeBuilder.end);
        when(treeBuilder.process(endTag)).thenReturn(true);
        
        boolean result = treeBuilder.processEndTag("name");
        
        verify(endTag, times(1)).reset();
        verify(endTag, times(1)).name("name");
        
        Mockito.verify(treeBuilder, times(1)).process(endTag);
        assert(result);
    }
    
    @Test
    public void testCurrentElement() {
        stack.add(mock(Element.class));
        
        Element element = treeBuilder.currentElement();
        
        Mockito.verify(stack, times(1)).size();
        verify(stack, times(1)).get(anyInt());
        
        assert(element != null);
    }
}