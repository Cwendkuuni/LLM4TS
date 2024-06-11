package JSci.maths.symbolic;

import java.io.*;

class SimpleCharStream
{
    public static final boolean staticFlag = true;
    static int bufsize;
    static int available;
    static int tokenBegin;
    public static int bufpos;
    protected static int[] bufline;
    protected static int[] bufcolumn;
    protected static int column;
    protected static int line;
    protected static boolean prevCharIsCR;
    protected static boolean prevCharIsLF;
    protected static Reader inputStream;
    protected static char[] buffer;
    protected static int maxNextCharInd;
    protected static int inBuf;
    
    protected static void ExpandBuff(final boolean wrapAround) {
        final char[] newbuffer = new char[SimpleCharStream.bufsize + 2048];
        final int[] newbufline = new int[SimpleCharStream.bufsize + 2048];
        final int[] newbufcolumn = new int[SimpleCharStream.bufsize + 2048];
        try {
            if (wrapAround) {
                System.arraycopy(SimpleCharStream.buffer, SimpleCharStream.tokenBegin, newbuffer, 0, SimpleCharStream.bufsize - SimpleCharStream.tokenBegin);
                System.arraycopy(SimpleCharStream.buffer, 0, newbuffer, SimpleCharStream.bufsize - SimpleCharStream.tokenBegin, SimpleCharStream.bufpos);
                SimpleCharStream.buffer = newbuffer;
                System.arraycopy(SimpleCharStream.bufline, SimpleCharStream.tokenBegin, newbufline, 0, SimpleCharStream.bufsize - SimpleCharStream.tokenBegin);
                System.arraycopy(SimpleCharStream.bufline, 0, newbufline, SimpleCharStream.bufsize - SimpleCharStream.tokenBegin, SimpleCharStream.bufpos);
                SimpleCharStream.bufline = newbufline;
                System.arraycopy(SimpleCharStream.bufcolumn, SimpleCharStream.tokenBegin, newbufcolumn, 0, SimpleCharStream.bufsize - SimpleCharStream.tokenBegin);
                System.arraycopy(SimpleCharStream.bufcolumn, 0, newbufcolumn, SimpleCharStream.bufsize - SimpleCharStream.tokenBegin, SimpleCharStream.bufpos);
                SimpleCharStream.bufcolumn = newbufcolumn;
                SimpleCharStream.maxNextCharInd = (SimpleCharStream.bufpos += SimpleCharStream.bufsize - SimpleCharStream.tokenBegin);
            }
            else {
                System.arraycopy(SimpleCharStream.buffer, SimpleCharStream.tokenBegin, newbuffer, 0, SimpleCharStream.bufsize - SimpleCharStream.tokenBegin);
                SimpleCharStream.buffer = newbuffer;
                System.arraycopy(SimpleCharStream.bufline, SimpleCharStream.tokenBegin, newbufline, 0, SimpleCharStream.bufsize - SimpleCharStream.tokenBegin);
                SimpleCharStream.bufline = newbufline;
                System.arraycopy(SimpleCharStream.bufcolumn, SimpleCharStream.tokenBegin, newbufcolumn, 0, SimpleCharStream.bufsize - SimpleCharStream.tokenBegin);
                SimpleCharStream.bufcolumn = newbufcolumn;
                SimpleCharStream.maxNextCharInd = (SimpleCharStream.bufpos -= SimpleCharStream.tokenBegin);
            }
        }
        catch (Throwable t) {
            throw new Error(t.getMessage());
        }
        SimpleCharStream.bufsize += 2048;
        SimpleCharStream.available = SimpleCharStream.bufsize;
        SimpleCharStream.tokenBegin = 0;
    }
    
    protected static void FillBuff() throws IOException {
        if (SimpleCharStream.maxNextCharInd == SimpleCharStream.available) {
            if (SimpleCharStream.available == SimpleCharStream.bufsize) {
                if (SimpleCharStream.tokenBegin > 2048) {
                    SimpleCharStream.bufpos = (SimpleCharStream.maxNextCharInd = 0);
                    SimpleCharStream.available = SimpleCharStream.tokenBegin;
                }
                else if (SimpleCharStream.tokenBegin < 0) {
                    SimpleCharStream.bufpos = (SimpleCharStream.maxNextCharInd = 0);
                }
                else {
                    ExpandBuff(false);
                }
            }
            else if (SimpleCharStream.available > SimpleCharStream.tokenBegin) {
                SimpleCharStream.available = SimpleCharStream.bufsize;
            }
            else if (SimpleCharStream.tokenBegin - SimpleCharStream.available < 2048) {
                ExpandBuff(true);
            }
            else {
                SimpleCharStream.available = SimpleCharStream.tokenBegin;
            }
        }
        try {
            final int i;
            if ((i = SimpleCharStream.inputStream.read(SimpleCharStream.buffer, SimpleCharStream.maxNextCharInd, SimpleCharStream.available - SimpleCharStream.maxNextCharInd)) == -1) {
                SimpleCharStream.inputStream.close();
                throw new IOException();
            }
            SimpleCharStream.maxNextCharInd += i;
        }
        catch (IOException e) {
            --SimpleCharStream.bufpos;
            backup(0);
            if (SimpleCharStream.tokenBegin == -1) {
                SimpleCharStream.tokenBegin = SimpleCharStream.bufpos;
            }
            throw e;
        }
    }
    
    public static char BeginToken() throws IOException {
        SimpleCharStream.tokenBegin = -1;
        final char c = readChar();
        SimpleCharStream.tokenBegin = SimpleCharStream.bufpos;
        return c;
    }
    
    protected static void UpdateLineColumn(final char c) {
        ++SimpleCharStream.column;
        if (SimpleCharStream.prevCharIsLF) {
            SimpleCharStream.prevCharIsLF = false;
            SimpleCharStream.line += (SimpleCharStream.column = 1);
        }
        else if (SimpleCharStream.prevCharIsCR) {
            SimpleCharStream.prevCharIsCR = false;
            if (c == '\n') {
                SimpleCharStream.prevCharIsLF = true;
            }
            else {
                SimpleCharStream.line += (SimpleCharStream.column = 1);
            }
        }
        switch (c) {
            case '\r': {
                SimpleCharStream.prevCharIsCR = true;
                break;
            }
            case '\n': {
                SimpleCharStream.prevCharIsLF = true;
                break;
            }
            case '\t': {
                --SimpleCharStream.column;
                SimpleCharStream.column += 8 - (SimpleCharStream.column & 0x7);
                break;
            }
        }
        SimpleCharStream.bufline[SimpleCharStream.bufpos] = SimpleCharStream.line;
        SimpleCharStream.bufcolumn[SimpleCharStream.bufpos] = SimpleCharStream.column;
    }
    
    public static char readChar() throws IOException {
        if (SimpleCharStream.inBuf > 0) {
            --SimpleCharStream.inBuf;
            if (++SimpleCharStream.bufpos == SimpleCharStream.bufsize) {
                SimpleCharStream.bufpos = 0;
            }
            return SimpleCharStream.buffer[SimpleCharStream.bufpos];
        }
        if (++SimpleCharStream.bufpos >= SimpleCharStream.maxNextCharInd) {
            FillBuff();
        }
        final char c = SimpleCharStream.buffer[SimpleCharStream.bufpos];
        UpdateLineColumn(c);
        return c;
    }
    
    public static int getColumn() {
        return SimpleCharStream.bufcolumn[SimpleCharStream.bufpos];
    }
    
    public static int getLine() {
        return SimpleCharStream.bufline[SimpleCharStream.bufpos];
    }
    
    public static int getEndColumn() {
        return SimpleCharStream.bufcolumn[SimpleCharStream.bufpos];
    }
    
    public static int getEndLine() {
        return SimpleCharStream.bufline[SimpleCharStream.bufpos];
    }
    
    public static int getBeginColumn() {
        return SimpleCharStream.bufcolumn[SimpleCharStream.tokenBegin];
    }
    
    public static int getBeginLine() {
        return SimpleCharStream.bufline[SimpleCharStream.tokenBegin];
    }
    
    public static void backup(final int amount) {
        SimpleCharStream.inBuf += amount;
        if ((SimpleCharStream.bufpos -= amount) < 0) {
            SimpleCharStream.bufpos += SimpleCharStream.bufsize;
        }
    }
    
    public SimpleCharStream(final Reader dstream, final int startline, final int startcolumn, final int buffersize) {
        if (SimpleCharStream.inputStream != null) {
            throw new Error("\n   ERROR: Second call to the constructor of a static SimpleCharStream.  You must\n       either use ReInit() or set the JavaCC option STATIC to false\n       during the generation of this class.");
        }
        SimpleCharStream.inputStream = dstream;
        SimpleCharStream.line = startline;
        SimpleCharStream.column = startcolumn - 1;
        SimpleCharStream.bufsize = buffersize;
        SimpleCharStream.available = buffersize;
        SimpleCharStream.buffer = new char[buffersize];
        SimpleCharStream.bufline = new int[buffersize];
        SimpleCharStream.bufcolumn = new int[buffersize];
    }
    
    public SimpleCharStream(final Reader dstream, final int startline, final int startcolumn) {
        this(dstream, startline, startcolumn, 4096);
    }
    
    public SimpleCharStream(final Reader dstream) {
        this(dstream, 1, 1, 4096);
    }
    
    public void ReInit(final Reader dstream, final int startline, final int startcolumn, final int buffersize) {
        SimpleCharStream.inputStream = dstream;
        SimpleCharStream.line = startline;
        SimpleCharStream.column = startcolumn - 1;
        if (SimpleCharStream.buffer == null || buffersize != SimpleCharStream.buffer.length) {
            SimpleCharStream.bufsize = buffersize;
            SimpleCharStream.available = buffersize;
            SimpleCharStream.buffer = new char[buffersize];
            SimpleCharStream.bufline = new int[buffersize];
            SimpleCharStream.bufcolumn = new int[buffersize];
        }
        SimpleCharStream.prevCharIsLF = (SimpleCharStream.prevCharIsCR = false);
        SimpleCharStream.tokenBegin = (SimpleCharStream.inBuf = (SimpleCharStream.maxNextCharInd = 0));
        SimpleCharStream.bufpos = -1;
    }
    
    public void ReInit(final Reader dstream, final int startline, final int startcolumn) {
        this.ReInit(dstream, startline, startcolumn, 4096);
    }
    
    public void ReInit(final Reader dstream) {
        this.ReInit(dstream, 1, 1, 4096);
    }
    
    public SimpleCharStream(final InputStream dstream, final int startline, final int startcolumn, final int buffersize) {
        this(new InputStreamReader(dstream), startline, startcolumn, 4096);
    }
    
    public SimpleCharStream(final InputStream dstream, final int startline, final int startcolumn) {
        this(dstream, startline, startcolumn, 4096);
    }
    
    public SimpleCharStream(final InputStream dstream) {
        this(dstream, 1, 1, 4096);
    }
    
    public void ReInit(final InputStream dstream, final int startline, final int startcolumn, final int buffersize) {
        this.ReInit(new InputStreamReader(dstream), startline, startcolumn, 4096);
    }
    
    public void ReInit(final InputStream dstream) {
        this.ReInit(dstream, 1, 1, 4096);
    }
    
    public void ReInit(final InputStream dstream, final int startline, final int startcolumn) {
        this.ReInit(dstream, startline, startcolumn, 4096);
    }
    
    public static String GetImage() {
        if (SimpleCharStream.bufpos >= SimpleCharStream.tokenBegin) {
            return new String(SimpleCharStream.buffer, SimpleCharStream.tokenBegin, SimpleCharStream.bufpos - SimpleCharStream.tokenBegin + 1);
        }
        return new String(SimpleCharStream.buffer, SimpleCharStream.tokenBegin, SimpleCharStream.bufsize - SimpleCharStream.tokenBegin) + new String(SimpleCharStream.buffer, 0, SimpleCharStream.bufpos + 1);
    }
    
    public static char[] GetSuffix(final int len) {
        final char[] ret = new char[len];
        if (SimpleCharStream.bufpos + 1 >= len) {
            System.arraycopy(SimpleCharStream.buffer, SimpleCharStream.bufpos - len + 1, ret, 0, len);
        }
        else {
            System.arraycopy(SimpleCharStream.buffer, SimpleCharStream.bufsize - (len - SimpleCharStream.bufpos - 1), ret, 0, len - SimpleCharStream.bufpos - 1);
            System.arraycopy(SimpleCharStream.buffer, 0, ret, len - SimpleCharStream.bufpos - 1, SimpleCharStream.bufpos + 1);
        }
        return ret;
    }
    
    public static void Done() {
        SimpleCharStream.buffer = null;
        SimpleCharStream.bufline = null;
        SimpleCharStream.bufcolumn = null;
    }
    
    public static void adjustBeginLineColumn(int newLine, final int newCol) {
        int start = SimpleCharStream.tokenBegin;
        int len;
        if (SimpleCharStream.bufpos >= SimpleCharStream.tokenBegin) {
            len = SimpleCharStream.bufpos - SimpleCharStream.tokenBegin + SimpleCharStream.inBuf + 1;
        }
        else {
            len = SimpleCharStream.bufsize - SimpleCharStream.tokenBegin + SimpleCharStream.bufpos + 1 + SimpleCharStream.inBuf;
        }
        int i = 0;
        int j = 0;
        int k = 0;
        int nextColDiff = 0;
        int columnDiff = 0;
        while (i < len && SimpleCharStream.bufline[j = start % SimpleCharStream.bufsize] == SimpleCharStream.bufline[k = ++start % SimpleCharStream.bufsize]) {
            SimpleCharStream.bufline[j] = newLine;
            nextColDiff = columnDiff + SimpleCharStream.bufcolumn[k] - SimpleCharStream.bufcolumn[j];
            SimpleCharStream.bufcolumn[j] = newCol + columnDiff;
            columnDiff = nextColDiff;
            ++i;
        }
        if (i < len) {
            SimpleCharStream.bufline[j] = newLine++;
            SimpleCharStream.bufcolumn[j] = newCol + columnDiff;
            while (i++ < len) {
                if (SimpleCharStream.bufline[j = start % SimpleCharStream.bufsize] != SimpleCharStream.bufline[++start % SimpleCharStream.bufsize]) {
                    SimpleCharStream.bufline[j] = newLine++;
                }
                else {
                    SimpleCharStream.bufline[j] = newLine;
                }
            }
        }
        SimpleCharStream.line = SimpleCharStream.bufline[j];
        SimpleCharStream.column = SimpleCharStream.bufcolumn[j];
    }
    
    static {
        SimpleCharStream.bufpos = -1;
        SimpleCharStream.column = 0;
        SimpleCharStream.line = 1;
        SimpleCharStream.prevCharIsCR = false;
        SimpleCharStream.prevCharIsLF = false;
        SimpleCharStream.maxNextCharInd = 0;
        SimpleCharStream.inBuf = 0;
    }
}
