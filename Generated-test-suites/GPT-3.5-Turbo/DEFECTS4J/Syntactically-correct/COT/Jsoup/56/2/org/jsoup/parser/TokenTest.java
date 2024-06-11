import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {



    @Test
    public void testTokenType() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };

        String expected = "Token";
        String actual = token.tokenType();

        assertEquals(expected, actual);
    }

    @Test
    public void testReset() {
        // TODO: Write test case for reset method
    }

    @Test
    public void testResetStringBuilder() {
        // TODO: Write test case for reset method with StringBuilder parameter
    }

    @Test
    public void testGetName() {
        // TODO: Write test case for getName method
    }

    @Test
    public void testGetPublicIdentifier() {
        // TODO: Write test case for getPublicIdentifier method
    }

    @Test
    public void testGetSystemIdentifier() {
        // TODO: Write test case for getSystemIdentifier method
    }

    @Test
    public void testIsForceQuirks() {
        // TODO: Write test case for isForceQuirks method
    }

    @Test
    public void testResetTag() {
        // TODO: Write test case for reset method in Tag class
    }

    @Test
    public void testNewAttribute() {
        // TODO: Write test case for newAttribute method
    }

    @Test
    public void testFinaliseTag() {
        // TODO: Write test case for finaliseTag method
    }

    @Test
    public void testName() {
        // TODO: Write test case for name method
    }

    @Test
    public void testNormalName() {
        // TODO: Write test case for normalName method
    }

    @Test
    public void testIsSelfClosing() {
        // TODO: Write test case for isSelfClosing method
    }

    @Test
    public void testGetAttributes() {
        // TODO: Write test case for getAttributes method
    }

    @Test
    public void testAppendTagNameString() {
        // TODO: Write test case for appendTagName method with String parameter
    }

    @Test
    public void testAppendTagNameChar() {
        // TODO: Write test case for appendTagName method with char parameter
    }

    @Test
    public void testAppendAttributeNameString() {
        // TODO: Write test case for appendAttributeName method with String parameter
    }

    @Test
    public void testAppendAttributeNameChar() {
        // TODO: Write test case for appendAttributeName method with char parameter
    }

    @Test
    public void testAppendAttributeValueString() {
        // TODO: Write test case for appendAttributeValue method with String parameter
    }

    @Test
    public void testAppendAttributeValueChar() {
        // TODO: Write test case for appendAttributeValue method with char parameter
    }

    @Test
    public void testAppendAttributeValueCharArray() {
        // TODO: Write test case for appendAttributeValue method with char[] parameter
    }

    @Test
    public void testAppendAttributeValueIntArray() {
        // TODO: Write test case for appendAttributeValue method with int[] parameter
    }

    @Test
    public void testSetEmptyAttributeValue() {
        // TODO: Write test case for setEmptyAttributeValue method
    }

    @Test
    public void testStartTagNameAttrStringAttributes() {
        // TODO: Write test case for nameAttr method in StartTag class
    }

    @Test
    public void testStartTagToString() {
        // TODO: Write test case for toString method in StartTag class
    }

    @Test
    public void testEndTagToString() {
        // TODO: Write test case for toString method in EndTag class
    }

    @Test
    public void testCommentGetData() {
        // TODO: Write test case for getData method in Comment class
    }

    @Test
    public void testCommentToString() {
        // TODO: Write test case for toString method in Comment class
    }

    @Test
    public void testCharacterGetData() {
        // TODO: Write test case for getData method in Character class
    }

    @Test
    public void testCharacterToString() {
        // TODO: Write test case for toString method in Character class
    }

    @Test
    public void testEOFReset() {
        // TODO: Write test case for reset method in EOF class
    }

    @Test
    public void testIsDoctype() {
        // TODO: Write test case for isDoctype method
    }

    @Test
    public void testAsDoctype() {
        // TODO: Write test case for asDoctype method
    }

    @Test
    public void testIsStartTag() {
        // TODO: Write test case for isStartTag method
    }

    @Test
    public void testAsStartTag() {
        // TODO: Write test case for asStartTag method
    }

    @Test
    public void testIsEndTag() {
        // TODO: Write test case for isEndTag method
    }

    @Test
    public void testAsEndTag() {
        // TODO: Write test case for asEndTag method
    }

    @Test
    public void testIsComment() {
        // TODO: Write test case for isComment method
    }

    @Test
    public void testAsComment() {
        // TODO: Write test case for asComment method
    }

    @Test
    public void testIsCharacter() {
        // TODO: Write test case for isCharacter method
    }

    @Test
    public void testAsCharacter() {
        // TODO: Write test case for asCharacter method
    }

    @Test
    public void testIsEOF() {
        // TODO: Write test case for isEOF method
    }
    


}