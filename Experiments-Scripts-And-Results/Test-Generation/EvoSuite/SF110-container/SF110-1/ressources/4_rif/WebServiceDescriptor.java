package com.densebrain.rif.server.transport;

public class WebServiceDescriptor
{
    String targetNamespace;
    String typesNamespace;
    Class serviceClazz;
    
    public WebServiceDescriptor(final Class serviceClazz, final String targetNamespace, final String typesNamespace) {
        this.serviceClazz = serviceClazz;
        this.targetNamespace = targetNamespace;
        this.typesNamespace = typesNamespace;
    }
    
    public Class getServiceClazz() {
        return this.serviceClazz;
    }
    
    public void setServiceClazz(final Class serviceClazz) {
        this.serviceClazz = serviceClazz;
    }
    
    public String getTargetNamespace() {
        return this.targetNamespace;
    }
    
    public void setTargetNamespace(final String targetNamespace) {
        this.targetNamespace = targetNamespace;
    }
    
    public String getTypesNamespace() {
        return this.typesNamespace;
    }
    
    public void setTypesNamespace(final String typesNamespace) {
        this.typesNamespace = typesNamespace;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = 31 * result + ((this.serviceClazz == null) ? 0 : this.serviceClazz.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final WebServiceDescriptor other = (WebServiceDescriptor)obj;
        if (this.serviceClazz == null) {
            if (other.serviceClazz != null) {
                return false;
            }
        }
        else if (this.serviceClazz != other.serviceClazz) {
            return false;
        }
        return true;
    }
}
