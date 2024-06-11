
import org.junit.Test;

public class SimpleCharStreamTest {

    @Test
    public void testExpandBuff() {
        SimpleCharStream.ExpandBuff(false);
        SimpleCharStream.ExpandBuff(true);
    }
    
    @Test
    public void testFillBuff() throws IOException {
        SimpleCharStream.FillBuff();
    }
    
    @Test
    public void testBeginToken() throws IOException {
        SimpleCharStream.BeginToken();
    }
    
    @Test
    public void testUpdateLineColumn() {
        SimpleCharStream.UpdateLineColumn('\n');
        SimpleCharStream.UpdateLineColumn('\r');
        SimpleCharStream.UpdateLineColumn('\t');
    }
    
    @Test
    public void testReadChar() throws IOException {
        SimpleCharStream.readChar();
    }
    
    @Test
    public void testGetColumn() {
        SimpleCharStream.getColumn();
    }
    
    @Test
    public void testGetLine() {
        SimpleCharStream.getLine();
    }
    
    @Test
    public void testGetEndColumn() {
        SimpleCharStream.getEndColumn();
    }
    
    @Test
    public void testGetEndLine() {
        SimpleCharStream.getEndLine();
    }
    
    @Test
    public void testGetBeginColumn() {
        SimpleCharStream.getBeginColumn();
    }
    
    @Test
    public void testGetBeginLine() {
        SimpleCharStream.getBeginLine();
    }
    
    @Test
    public void testBackup() {
        SimpleCharStream.backup(0);
    }
    
    @Test
    public void testReInit() {
        SimpleCharStream.ReInit(new FileReader("test.txt"), 1, 1, 4096);
    }
    
    @Test
    public void testGetImage() {
        SimpleCharStream.GetImage();
    }
    
    @Test
    public void testGetSuffix() {
        SimpleCharStream.GetSuffix(5);
    }
    
    @Test
    public void testDone() {
        SimpleCharStream.Done();
    }
    
    @Test
    public void testAdjustBeginLineColumn() {
        SimpleCharStream.adjustBeginLineColumn(1, 1);
    }
}
