package twitter4j;

import java.io.*;

final class ExceptionDiagnosis implements Serializable
{
    int stackLineHash;
    int lineNumberHash;
    String hexString;
    Throwable th;
    private static final long serialVersionUID = 453958937114285988L;
    
    ExceptionDiagnosis(final Throwable th) {
        this(th, new String[0]);
    }
    
    ExceptionDiagnosis(final Throwable th, final String[] inclusionFilter) {
        this.hexString = "";
        this.th = th;
        final StackTraceElement[] stackTrace = th.getStackTrace();
        this.stackLineHash = 0;
        this.lineNumberHash = 0;
        for (int i = stackTrace.length - 1; i >= 0; --i) {
            final StackTraceElement line = stackTrace[i];
            for (final String filter : inclusionFilter) {
                if (line.getClassName().startsWith(filter)) {
                    final int hash = line.getClassName().hashCode() + line.getMethodName().hashCode();
                    this.stackLineHash = 31 * this.stackLineHash + hash;
                    this.lineNumberHash = 31 * this.lineNumberHash + line.getLineNumber();
                    break;
                }
            }
        }
        this.hexString = this.hexString + this.toHexString(this.stackLineHash) + "-" + this.toHexString(this.lineNumberHash);
        if (null != th.getCause()) {
            this.hexString = this.hexString + " " + new ExceptionDiagnosis(th.getCause(), inclusionFilter).asHexString();
        }
    }
    
    int getStackLineHash() {
        return this.stackLineHash;
    }
    
    int getLineNumberHash() {
        return this.lineNumberHash;
    }
    
    String asHexString() {
        return this.hexString;
    }
    
    private String toHexString(final int value) {
        final String str = "0000000" + Integer.toHexString(value);
        return str.substring(str.length() - 8, str.length());
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ExceptionDiagnosis that = (ExceptionDiagnosis)o;
        return this.lineNumberHash == that.lineNumberHash && this.stackLineHash == that.stackLineHash;
    }
    
    public int hashCode() {
        int result = this.stackLineHash;
        result = 31 * result + this.lineNumberHash;
        return result;
    }
    
    public String toString() {
        return "ExceptionDiagnosis{stackLineHash=" + this.stackLineHash + ", lineNumberHash=" + this.lineNumberHash + '}';
    }
}
