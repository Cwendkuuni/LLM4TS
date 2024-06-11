import org.junit.Test;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.CharacterReferenceException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

public class TokeniserTest {
    @Test
    public void testRead() {
        // test reading from an empty input stream
        Tokeniser tokeniser = new Tokeniser(new CharacterReader() {
            @Override
            public int read() throws IOException {
                return -1;
            }

            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                return -1;
            }
        }, new ArrayList<>());

        assertNull(tokeniser.read());

        // test reading past the end of the input stream
        CharacterReader reader = new CharacterReader() {
            private int pos = 0;
            private char current = 'a';

            @Override
            public int read() throws IOException {
                if (pos >= 1) {
                    pos++;
                    return current;
                }
                return -1;
            }

            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                int length = Math.min(len, 1 - off);
                System.arraycopy(new char[]{current}, 0, cbuf, off, length);
                pos++;
                return length;
            }
        };
        Tokeniser tokeniser = new Tokeniser(reader, new ArrayList<>());
        assertNull(tokeniser.read());
        assertNull(tokeniser.read());
        assertNull(tokeniser.read());
        assertNull(tokeniser.read());
        assertNull(tokeniser.read());

        // test reading invalid characters
        reader = new CharacterReader() {
            @Override
            public int read() throws IOException {
                return 0xFFFD; // null character
            }

            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new UnsupportedOperationException();
            }
        };
        Tokeniser tokeniser = new Tokeniser(reader, new ArrayList<>());
        assertNull(tokeniser.read());
        tokeniser.error(TokeniserState.Data);
    }

    @Test
    public void testEmit() {
        // test emitting a valid token
        Tokeniser tokeniser = new Tokeniser(new CharacterReader() {
            @Override
            public int read() throws IOException {
                return 'a';
            }

            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new UnsupportedOperationException();
            }
        }, new ArrayList<>());

        tokeniser.emit(new Token.Character('a'));
        assertNotNull(tokeniser.read());
        assertEquals("a", tokeniser.read().getData());

        // test emitting a null token
        assertThrows(IllegalArgumentException.class, () -> tokeniser.emit(null));

        // test emitting a token with an invalid type
        tokeniser.emit(new Token.Comment());
        assertNull(tokeniser.read());
    }

    // ... other test cases for remaining methods
}