package JSci;

import java.util.*;
import java.net.*;
import java.io.*;

public final class Version implements Serializable
{
    public final int major;
    public final int minor;
    public final String platform;
    public final String home;
    
    public static Version getCurrent() {
        final ResourceBundle bundle = ResourceBundle.getBundle("JSci.Bundle");
        return new Version(Integer.parseInt(bundle.getString("version.major")), Integer.parseInt(bundle.getString("version.minor")), bundle.getString("version.home"), bundle.getString("version.platform"));
    }
    
    public static Version getLatest() throws IOException {
        Version version = null;
        try {
            final ObjectInputStream objectInputStream = new ObjectInputStream(new URL(getCurrent().home + "version.ser").openStream());
            version = (Version)objectInputStream.readObject();
            objectInputStream.close();
        }
        catch (MalformedURLException ex) {}
        catch (ClassNotFoundException ex2) {}
        return version;
    }
    
    private Version(final int major, final int minor, final String home, final String platform) {
        this.major = major;
        this.minor = minor;
        this.home = home;
        this.platform = platform;
    }
    
    public boolean equals(final Object o) {
        if (!(o instanceof Version)) {
            return false;
        }
        final Version version = (Version)o;
        return this.major == version.major && this.minor == version.minor && this.platform.equals(version.platform);
    }
    
    public int hashCode() {
        return 37 * (37 * (629 + this.major) + this.minor) + this.platform.hashCode();
    }
    
    public String toString() {
        return new StringBuffer().append(this.major).append('.').append(this.minor).toString();
    }
    
    public boolean isLater(final Version version) {
        return this.major > version.major || (this.major == version.major && this.minor > version.minor);
    }
}
