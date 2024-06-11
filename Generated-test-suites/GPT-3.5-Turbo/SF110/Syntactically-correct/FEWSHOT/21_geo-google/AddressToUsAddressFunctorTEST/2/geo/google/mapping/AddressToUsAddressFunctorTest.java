package geo.google.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import geo.google.datamodel.*;
import geo.google.*;
import org.apache.commons.collections.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import java.util.*;

import org.junit.Test;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testExecute() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        
        GeoAddress addr = new GeoAddress();
        AddressDetails details = new AddressDetails();
        addr.setAddressDetails(details);
        
        GeoUsAddress result = functor.execute(addr);
        assertNull(result.getCountry());
        assertNull(result.getState());
        assertNull(result.getCounty());
        assertNull(result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
        
        AddressDetails.Country country = new AddressDetails.Country();
        country.setCountryNameCode(new ArrayList<>());
        details.setCountry(country);
        
        result = functor.execute(addr);
        assertNull(result.getCountry());
        assertNull(result.getState());
        assertNull(result.getCounty());
        assertNull(result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
        
        AddressDetails.Country.CountryNameCode countryCode = new AddressDetails.Country.CountryNameCode();
        country.getCountryNameCode().add(countryCode);
        
        countryCode.setContent("US");
        
        result = functor.execute(addr);
        assertEquals("US", result.getCountry());
        assertNull(result.getState());
        assertNull(result.getCounty());
        assertNull(result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
        
        AdministrativeArea adminArea = new AdministrativeArea();
        country.setAdministrativeArea(adminArea);
        
        result = functor.execute(addr);
        assertEquals("US", result.getCountry());
        assertNull(result.getState());
        assertNull(result.getCounty());
        assertNull(result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
        
        AdministrativeArea.AdministrativeAreaName adminAreaName = new AdministrativeArea.AdministrativeAreaName();
        adminArea.setAdministrativeAreaName(new ArrayList<>());
        adminArea.getAdministrativeAreaName().add(adminAreaName);
        
        adminAreaName.setContent("California");
        
        result = functor.execute(addr);
        assertEquals("US", result.getCountry());
        assertEquals("California", result.getState());
        assertNull(result.getCounty());
        assertNull(result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
        
        AdministrativeArea.SubAdministrativeArea subAdminArea = new AdministrativeArea.SubAdministrativeArea();
        adminArea.setSubAdministrativeArea(subAdminArea);
        
        result = functor.execute(addr);
        assertEquals("US", result.getCountry());
        assertEquals("California", result.getState());
        assertNull(result.getCounty());
        assertNull(result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
        
        AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName subAdminAreaName = new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName();
        subAdminArea.setSubAdministrativeAreaName(new ArrayList<>());
        subAdminArea.getSubAdministrativeAreaName().add(subAdminAreaName);
        
        subAdminAreaName.setContent("Los Angeles");
        
        result = functor.execute(addr);
        assertEquals("US", result.getCountry());
        assertEquals("California", result.getState());
        assertEquals("Los Angeles", result.getCounty());
        assertNull(result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
        
        Locality locality = new Locality();
        subAdminArea.setLocality(locality);
        
        result = functor.execute(addr);
        assertEquals("US", result.getCountry());
        assertEquals("California", result.getState());
        assertEquals("Los Angeles", result.getCounty());
        assertNull(result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
        
        Locality.LocalityName localityName = new Locality.LocalityName();
        locality.setLocalityName(new ArrayList<>());
        locality.getLocalityName().add(localityName);
        
        localityName.setContent("Los Angeles");
        
        result = functor.execute(addr);
        assertEquals("US", result.getCountry());
        assertEquals("California", result.getState());
        assertEquals("Los Angeles", result.getCounty());
        assertEquals("Los Angeles", result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
        
        PostalCode postalCode = new PostalCode();
        locality.setPostalCode(postalCode);
        
        result = functor.execute(addr);
        assertEquals("US", result.getCountry());
        assertEquals("California", result.getState());
        assertEquals("Los Angeles", result.getCounty());
        assertEquals("Los Angeles", result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
        
        PostalCode.PostalCodeNumber postalCodeNumber = new PostalCode.PostalCodeNumber();
        postalCode.setPostalCodeNumber(new ArrayList<>());
        postalCode.getPostalCodeNumber().add(postalCodeNumber);
        
        postalCodeNumber.setContent("90001");
        
        result = functor.execute(addr);
        assertEquals("US", result.getCountry());
        assertEquals("California", result.getState());
        assertEquals("Los Angeles", result.getCounty());
        assertEquals("Los Angeles", result.getCity());
        assertEquals("90001", result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
        
        Thoroughfare thoroughfare = new Thoroughfare();
        locality.setThoroughfare(thoroughfare);
        
        result = functor.execute(addr);
        assertEquals("US", result.getCountry());
        assertEquals("California", result.getState());
        assertEquals("Los Angeles", result.getCounty());
        assertEquals("Los Angeles", result.getCity());
        assertEquals("90001", result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
        
        ThoroughfareNameType thoroughfareName = new ThoroughfareNameType();
        thoroughfare.setThoroughfareName(new ArrayList<>());
        thoroughfare.getThoroughfareName().add(thoroughfareName);
        
        thoroughfareName.setContent("Main Street");
        
        result = functor.execute(addr);
        assertEquals("US", result.getCountry());
        assertEquals("California", result.getState());
        assertEquals("Los Angeles", result.getCounty());
        assertEquals("Los Angeles", result.getCity());
        assertEquals("90001", result.getPostalCode());
        assertEquals("Main Street", result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
        
        ThoroughfareNameType thoroughfareName2 = new ThoroughfareNameType();
        thoroughfare.getThoroughfareName().add(thoroughfareName2);
        
        thoroughfareName2.setContent("Second Street");
        
        result = functor.execute(addr);
        assertEquals("US", result.getCountry());
        assertEquals("California", result.getState());
        assertEquals("Los Angeles", result.getCounty());
        assertEquals("Los Angeles", result.getCity());
        assertEquals("90001", result.getPostalCode());
        assertEquals("Main Street", result.getAddressLine1());
        assertEquals("Second Street", result.getAddressLine2());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
        
        Coordinate coordinate = new Coordinate();
        addr.setCoordinate(coordinate);
        
        result = functor.execute(addr);
        assertEquals("US", result.getCountry());
        assertEquals("California", result.getState());
        assertEquals("Los Angeles", result.getCounty());
        assertEquals("Los Angeles", result.getCity());
        assertEquals("90001", result.getPostalCode());
        assertEquals("Main Street", result.getAddressLine1());
        assertEquals("Second Street", result.getAddressLine2());
        assertEquals(coordinate, result.getCoordinate());
        assertNull(result.getAccuracy());
        
        coordinate.setAccuracy(90);
        
        result = functor.execute(addr);
        assertEquals("US", result.getCountry());
        assertEquals("California", result.getState());
        assertEquals("Los Angeles", result.getCounty());
        assertEquals("Los Angeles", result.getCity());
        assertEquals("90001", result.getPostalCode());
        assertEquals("Main Street", result.getAddressLine1());
        assertEquals("Second Street", result.getAddressLine2());
        assertEquals(coordinate, result.getCoordinate());
        assertEquals(90, result.getAccuracy());
    }
}