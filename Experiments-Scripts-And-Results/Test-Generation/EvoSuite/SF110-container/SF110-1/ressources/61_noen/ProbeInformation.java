package fi.vtt.noen.mfw.bundle.probe.shared;

import fi.vtt.noen.mfw.bundle.common.*;

public class ProbeInformation
{
    private final String targetName;
    private final String targetType;
    private final String bmClass;
    private final String bmName;
    private final String bmDescription;
    private final String probeName;
    private final int precision;
    private final String xmlRpcUrl;
    
    public ProbeInformation(final String targetName, final String targetType, final String bmClass, final String bmName, final String bmDescription, final String probeDescription, final int precision, final String xmlRpcUrl) {
        this.targetName = targetName;
        this.targetType = targetType;
        this.bmClass = bmClass;
        this.bmName = bmName;
        this.bmDescription = bmDescription;
        this.probeName = probeDescription;
        this.precision = precision;
        this.xmlRpcUrl = xmlRpcUrl;
    }
    
    public String getTargetType() {
        return this.targetType;
    }
    
    public String getBmName() {
        return this.bmName;
    }
    
    public String getProbeName() {
        return this.probeName;
    }
    
    public String getTargetName() {
        return this.targetName;
    }
    
    public String getBmClass() {
        return this.bmClass;
    }
    
    public String getBmDescription() {
        return this.bmDescription;
    }
    
    public int getPrecision() {
        return this.precision;
    }
    
    public String getMeasureURI() {
        return Const.createMeasureURI(this.targetType, this.targetName, this.bmClass, this.bmName);
    }
    
    public String getXmlRpcUrl() {
        return this.xmlRpcUrl;
    }
    
    @Override
    public String toString() {
        return "ProbeInformation{targetName='" + this.targetName + '\'' + ", targetType='" + this.targetType + '\'' + ", bmClass='" + this.bmClass + '\'' + ", bmName='" + this.bmName + '\'' + ", bmDescription='" + this.bmDescription + '\'' + ", probeDescription='" + this.probeName + '\'' + ", precision=" + this.precision + '}';
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ProbeInformation that = (ProbeInformation)o;
        if (this.precision != that.precision) {
            return false;
        }
        Label_0075: {
            if (this.bmClass != null) {
                if (this.bmClass.equals(that.bmClass)) {
                    break Label_0075;
                }
            }
            else if (that.bmClass == null) {
                break Label_0075;
            }
            return false;
        }
        Label_0108: {
            if (this.bmDescription != null) {
                if (this.bmDescription.equals(that.bmDescription)) {
                    break Label_0108;
                }
            }
            else if (that.bmDescription == null) {
                break Label_0108;
            }
            return false;
        }
        Label_0141: {
            if (this.bmName != null) {
                if (this.bmName.equals(that.bmName)) {
                    break Label_0141;
                }
            }
            else if (that.bmName == null) {
                break Label_0141;
            }
            return false;
        }
        Label_0174: {
            if (this.probeName != null) {
                if (this.probeName.equals(that.probeName)) {
                    break Label_0174;
                }
            }
            else if (that.probeName == null) {
                break Label_0174;
            }
            return false;
        }
        Label_0207: {
            if (this.targetName != null) {
                if (this.targetName.equals(that.targetName)) {
                    break Label_0207;
                }
            }
            else if (that.targetName == null) {
                break Label_0207;
            }
            return false;
        }
        if (this.targetType != null) {
            if (this.targetType.equals(that.targetType)) {
                return true;
            }
        }
        else if (that.targetType == null) {
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = (this.targetName != null) ? this.targetName.hashCode() : 0;
        result = 31 * result + ((this.targetType != null) ? this.targetType.hashCode() : 0);
        result = 31 * result + ((this.bmClass != null) ? this.bmClass.hashCode() : 0);
        result = 31 * result + ((this.bmName != null) ? this.bmName.hashCode() : 0);
        result = 31 * result + ((this.bmDescription != null) ? this.bmDescription.hashCode() : 0);
        result = 31 * result + ((this.probeName != null) ? this.probeName.hashCode() : 0);
        result = 31 * result + this.precision;
        return result;
    }
}
