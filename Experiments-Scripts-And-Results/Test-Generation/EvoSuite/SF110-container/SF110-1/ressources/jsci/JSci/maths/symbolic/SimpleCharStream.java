package JSci.maths.symbolic;

import java.io.*;

public class SimpleCharStream
{
    public static final boolean staticFlag = true;
    public static int bufsize;
    public static int available;
    public static int tokenBegin;
    public static int bufpos;
    public static int[] bufline;
    public static int[] bufcolumn;
    public static int column;
    public static int line;
    public static boolean prevCharIsCR;
    public static boolean prevCharIsLF;
    public static Reader inputStream;
    public static char[] buffer;
    public static int maxNextCharInd;
    public static int inBuf;
    
    public static void ExpandBuff(final boolean b) {
        final char[] array = new char[SimpleCharStream.bufsize + 2048];
        final int[] array2 = new int[SimpleCharStream.bufsize + 2048];
        final int[] array3 = new int[SimpleCharStream.bufsize + 2048];
        try {
            if (b) {
                System.arraycopy(SimpleCharStream.buffer, SimpleCharStream.tokenBegin, array, 0, SimpleCharStream.bufsize - SimpleCharStream.tokenBegin);
                System.arraycopy(SimpleCharStream.buffer, 0, array, SimpleCharStream.bufsize - SimpleCharStream.tokenBegin, SimpleCharStream.bufpos);
                SimpleCharStream.buffer = array;
                System.arraycopy(SimpleCharStream.bufline, SimpleCharStream.tokenBegin, array2, 0, SimpleCharStream.bufsize - SimpleCharStream.tokenBegin);
                System.arraycopy(SimpleCharStream.bufline, 0, array2, SimpleCharStream.bufsize - SimpleCharStream.tokenBegin, SimpleCharStream.bufpos);
                SimpleCharStream.bufline = array2;
                System.arraycopy(SimpleCharStream.bufcolumn, SimpleCharStream.tokenBegin, array3, 0, SimpleCharStream.bufsize - SimpleCharStream.tokenBegin);
                System.arraycopy(SimpleCharStream.bufcolumn, 0, array3, SimpleCharStream.bufsize - SimpleCharStream.tokenBegin, SimpleCharStream.bufpos);
                SimpleCharStream.bufcolumn = array3;
                SimpleCharStream.maxNextCharInd = (SimpleCharStream.bufpos += SimpleCharStream.bufsize - SimpleCharStream.tokenBegin);
            }
            else {
                System.arraycopy(SimpleCharStream.buffer, SimpleCharStream.tokenBegin, array, 0, SimpleCharStream.bufsize - SimpleCharStream.tokenBegin);
                SimpleCharStream.buffer = array;
                System.arraycopy(SimpleCharStream.bufline, SimpleCharStream.tokenBegin, array2, 0, SimpleCharStream.bufsize - SimpleCharStream.tokenBegin);
                SimpleCharStream.bufline = array2;
                System.arraycopy(SimpleCharStream.bufcolumn, SimpleCharStream.tokenBegin, array3, 0, SimpleCharStream.bufsize - SimpleCharStream.tokenBegin);
                SimpleCharStream.bufcolumn = array3;
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
    
    public static void FillBuff() throws IOException {
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
            final int read;
            if ((read = SimpleCharStream.inputStream.read(SimpleCharStream.buffer, SimpleCharStream.maxNextCharInd, SimpleCharStream.available - SimpleCharStream.maxNextCharInd)) == -1) {
                SimpleCharStream.inputStream.close();
                throw new IOException();
            }
            SimpleCharStream.maxNextCharInd += read;
        }
        catch (IOException ex) {
            --SimpleCharStream.bufpos;
            backup(0);
            if (SimpleCharStream.tokenBegin == -1) {
                SimpleCharStream.tokenBegin = SimpleCharStream.bufpos;
            }
            throw ex;
        }
    }
    
    public static char BeginToken() throws IOException {
        SimpleCharStream.tokenBegin = -1;
        final char char1 = readChar();
        SimpleCharStream.tokenBegin = SimpleCharStream.bufpos;
        return char1;
    }
    
    public static void UpdateLineColumn(final char c) {
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
    
    public static void backup(final int n) {
        SimpleCharStream.inBuf += n;
        if ((SimpleCharStream.bufpos -= n) < 0) {
            SimpleCharStream.bufpos += SimpleCharStream.bufsize;
        }
    }
    
    public SimpleCharStream(final Reader inputStream, final int line, final int n, final int n2) {
        if (SimpleCharStream.inputStream != null) {
            throw new Error("\n   ERROR: Second call to the constructor of a static SimpleCharStream.  You must\n       either use ReInit() or set the JavaCC option STATIC to false\n       during the generation of this class.");
        }
        SimpleCharStream.inputStream = inputStream;
        SimpleCharStream.line = line;
        SimpleCharStream.column = n - 1;
        SimpleCharStream.bufsize = n2;
        SimpleCharStream.available = n2;
        SimpleCharStream.buffer = new char[n2];
        SimpleCharStream.bufline = new int[n2];
        SimpleCharStream.bufcolumn = new int[n2];
    }
    
    public SimpleCharStream(final Reader reader, final int n, final int n2) {
        this(reader, n, n2, 4096);
    }
    
    public SimpleCharStream(final Reader reader) {
        this(reader, 1, 1, 4096);
    }
    
    public static void ReInit(final Reader inputStream, final int line, final int n, final int n2) {
        SimpleCharStream.inputStream = inputStream;
        SimpleCharStream.line = line;
        SimpleCharStream.column = n - 1;
        if (SimpleCharStream.buffer == null || n2 != SimpleCharStream.buffer.length) {
            SimpleCharStream.bufsize = n2;
            SimpleCharStream.available = n2;
            SimpleCharStream.buffer = new char[n2];
            SimpleCharStream.bufline = new int[n2];
            SimpleCharStream.bufcolumn = new int[n2];
        }
        SimpleCharStream.prevCharIsLF = (SimpleCharStream.prevCharIsCR = false);
        SimpleCharStream.tokenBegin = (SimpleCharStream.inBuf = (SimpleCharStream.maxNextCharInd = 0));
        SimpleCharStream.bufpos = -1;
    }
    
    public static void ReInit(final Reader reader, final int n, final int n2) {
        ReInit(reader, n, n2, 4096);
    }
    
    public static void ReInit(final Reader reader) {
        ReInit(reader, 1, 1, 4096);
    }
    
    public SimpleCharStream(final InputStream inputStream, final int n, final int n2, final int n3) {
        this(new InputStreamReader(inputStream), n, n2, 4096);
    }
    
    public SimpleCharStream(final InputStream inputStream, final int n, final int n2) {
        this(inputStream, n, n2, 4096);
    }
    
    public SimpleCharStream(final InputStream inputStream) {
        this(inputStream, 1, 1, 4096);
    }
    
    public static void ReInit(final InputStream inputStream, final int n, final int n2, final int n3) {
        ReInit(new InputStreamReader(inputStream), n, n2, 4096);
    }
    
    public void ReInit(final InputStream inputStream) {
        ReInit(inputStream, 1, 1, 4096);
    }
    
    public void ReInit(final InputStream inputStream, final int n, final int n2) {
        ReInit(inputStream, n, n2, 4096);
    }
    
    public static String GetImage() {
        if (SimpleCharStream.bufpos >= SimpleCharStream.tokenBegin) {
            return new String(SimpleCharStream.buffer, SimpleCharStream.tokenBegin, SimpleCharStream.bufpos - SimpleCharStream.tokenBegin + 1);
        }
        return new String(SimpleCharStream.buffer, SimpleCharStream.tokenBegin, SimpleCharStream.bufsize - SimpleCharStream.tokenBegin) + new String(SimpleCharStream.buffer, 0, SimpleCharStream.bufpos + 1);
    }
    
    public static char[] GetSuffix(final int n) {
        final char[] array = new char[n];
        if (SimpleCharStream.bufpos + 1 >= n) {
            System.arraycopy(SimpleCharStream.buffer, SimpleCharStream.bufpos - n + 1, array, 0, n);
        }
        else {
            System.arraycopy(SimpleCharStream.buffer, SimpleCharStream.bufsize - (n - SimpleCharStream.bufpos - 1), array, 0, n - SimpleCharStream.bufpos - 1);
            System.arraycopy(SimpleCharStream.buffer, 0, array, n - SimpleCharStream.bufpos - 1, SimpleCharStream.bufpos + 1);
        }
        return array;
    }
    
    public static void Done() {
        SimpleCharStream.buffer = null;
        SimpleCharStream.bufline = null;
        SimpleCharStream.bufcolumn = null;
    }
    
    public static void adjustBeginLineColumn(int n, final int n2) {
        int tokenBegin = SimpleCharStream.tokenBegin;
        int n3;
        if (SimpleCharStream.bufpos >= SimpleCharStream.tokenBegin) {
            n3 = SimpleCharStream.bufpos - SimpleCharStream.tokenBegin + SimpleCharStream.inBuf + 1;
        }
        else {
            n3 = SimpleCharStream.bufsize - SimpleCharStream.tokenBegin + SimpleCharStream.bufpos + 1 + SimpleCharStream.inBuf;
        }
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int n7;
        while (n4 < n3 && SimpleCharStream.bufline[n5 = tokenBegin % SimpleCharStream.bufsize] == SimpleCharStream.bufline[n7 = ++tokenBegin % SimpleCharStream.bufsize]) {
            SimpleCharStream.bufline[n5] = n;
            final int n8 = n6 + SimpleCharStream.bufcolumn[n7] - SimpleCharStream.bufcolumn[n5];
            SimpleCharStream.bufcolumn[n5] = n2 + n6;
            n6 = n8;
            ++n4;
        }
        if (n4 < n3) {
            SimpleCharStream.bufline[n5] = n++;
            SimpleCharStream.bufcolumn[n5] = n2 + n6;
            while (n4++ < n3) {
                if (SimpleCharStream.bufline[n5 = tokenBegin % SimpleCharStream.bufsize] != SimpleCharStream.bufline[++tokenBegin % SimpleCharStream.bufsize]) {
                    SimpleCharStream.bufline[n5] = n++;
                }
                else {
                    SimpleCharStream.bufline[n5] = n;
                }
            }
        }
        SimpleCharStream.line = SimpleCharStream.bufline[n5];
        SimpleCharStream.column = SimpleCharStream.bufcolumn[n5];
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
